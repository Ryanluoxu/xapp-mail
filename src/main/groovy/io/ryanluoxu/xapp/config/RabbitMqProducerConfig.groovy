package io.ryanluoxu.xapp.config

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.ryanluoxu.xapp.service.MqProducerService
import io.ryanluoxu.xapp.service.impl.MqProducerServiceImpl
import io.ryanluoxu.xapp.util.JsonUtil
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.MessageConversionException
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.amqp.support.converter.SimpleMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author luoxu on 10/19
 */
@CompileStatic
@Configuration
@EnableRabbit
@Slf4j
class RabbitMqProducerConfig {

    @Value('${spring.rabbitmq.exchange}')
    String exchange

    /**
     * produce json msg
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory)
        template.exchange = exchange
        MessageConverter customMessageConverter = new SimpleMessageConverter(){
            @Override
            protected Message createMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
                String stringMessage = (object instanceof String)? object: JsonUtil.toString(object)
                log.info("RabbitMQ push message -> ${stringMessage}")
                return super.createMessage(stringMessage, messageProperties)
            }
        }
        template.setMessageConverter(customMessageConverter)
        return template
    }

    @Bean
    MqProducerService mqProducerService(RabbitTemplate rabbitTemplate){
        return new MqProducerServiceImpl(rabbitTemplate, exchange)
    }
}
