package io.ryanluoxu.xapp.config

import com.fasterxml.jackson.core.JsonParseException
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.ryanluoxu.xapp.util.JsonUtil
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener
import org.springframework.retry.RetryPolicy
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.interceptor.RetryInterceptorBuilder
import org.springframework.retry.interceptor.RetryOperationsInterceptor
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

/**
 * @author luoxu on 10/19
 */
@CompileStatic
@Configuration
@EnableRabbit
@Slf4j
class RabbitMqListenerConfig implements RabbitListenerConfigurer {

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory()
        factory.connectionFactory = connectionFactory
        factory.concurrentConsumers = 2
        factory.maxConcurrentConsumers = 4
        factory.recoveryInterval = 1000L
        factory.setAdviceChain(retryOperationsInterceptor())
        return factory
    }

    @Bean
    RetryOperationsInterceptor retryOperationsInterceptor(){
        RetryTemplate retryTemplate = new RetryTemplate()
        Map<Class<? extends Throwable>, Boolean> retryableMap = new HashMap<>()
        retryableMap.put(JsonParseException, false)
        RetryPolicy retryPolicy = new SimpleRetryPolicy(Integer.MAX_VALUE, retryableMap)
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy()
        backOffPolicy.setInitialInterval(1000)
        backOffPolicy.setMultiplier(10)
        backOffPolicy.setMaxInterval(3600000)
        retryTemplate.setRetryPolicy(retryPolicy)
        retryTemplate.setBackOffPolicy(backOffPolicy)
        retryTemplate.registerListener(
            new RetryListener() {
                @Override
                <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                    return true
                }
                @Override
                <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                    if (throwable!=null){
                        log.error("Failed: Retry count " + context.getRetryCount(), throwable)
                    }
                }
                @Override
                <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                    log.error("Retry count " + context.getRetryCount(), throwable)
                }
            }
        )
        RetryOperationsInterceptor interceptor = RetryInterceptorBuilder.stateless()
            .retryOperations(retryTemplate)
            .build()
        return interceptor
    }

    @Override
    void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory())
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory(){
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory()
        messageHandlerMethodFactory.setArgumentResolvers([
            new HandlerMethodArgumentResolver(){
                @Override
                boolean supportsParameter(MethodParameter parameter) {
                    return true
                }

                @Override
                Object resolveArgument(MethodParameter parameter, org.springframework.messaging.Message<?> message) throws Exception {
                    if (parameter.parameterName == "routingKey"){
                        return message?.headers?.get("amqp_receivedRoutingKey") as String
                    }
                    try {
                        Object payload = message.getPayload()
                        if (payload instanceof byte[]){
                            return JsonUtil.parse(new String(payload), parameter.getParameterType() as Class<Object>)
                        }
                        return JsonUtil.parse(payload.toString(), parameter.genericParameterType as Class<Object>)
                    } catch (Exception ex){
                        log.error("Invalid message,  abort listen!", ex)
                    }
                }
            } as HandlerMethodArgumentResolver
        ])
        return messageHandlerMethodFactory
    }
}
