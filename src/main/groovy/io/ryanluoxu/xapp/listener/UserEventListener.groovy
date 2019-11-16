package io.ryanluoxu.xapp.listener

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.ryanluoxu.xapp.listener.msg.UserMsg
import io.ryanluoxu.xapp.util.JsonUtil
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Argument
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

/**
 * @author luoxu on 11/19
 */
@CompileStatic
@Component
@Slf4j
class UserEventListener {

    @RabbitListener(
        bindings = @QueueBinding(
                key = 'xapp.user.status.change',
                value = @Queue(value = 'xapp.user.status', durable = 'true', autoDelete = 'false', arguments = [@Argument(name = 'x-message-ttl', value = '3600000', type = 'java.lang.Long')]),
                exchange = @Exchange(value = 'exchange.xapp', type = ExchangeTypes.TOPIC, durable = 'true')
        ),
        containerFactory = 'rabbitListenerContainerFactory'
    )
    void userStatusChange(UserMsg userMsg){
        log.info("UserEventListener userStatusChange - userMsg: ${JsonUtil.toString(userMsg)}")
    }
}
