package io.ryanluoxu.xapp.service.impl

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.ryanluoxu.xapp.entity.UserInfo
import io.ryanluoxu.xapp.listener.msg.UserMsg
import io.ryanluoxu.xapp.service.MqProducerService
import org.springframework.amqp.rabbit.core.RabbitTemplate

/**
 * @author luoxu on 11/19
 */
@CompileStatic
@Slf4j
class MqProducerServiceImpl implements MqProducerService {

    RabbitTemplate rabbitTemplate
    String exchange

    static final String ROUTING_KEY_USER_STATUS_CHANGE = "xapp.user.status.change"

    MqProducerServiceImpl(RabbitTemplate rabbitTemplate, String exchange){
        this.rabbitTemplate = rabbitTemplate
        this.exchange = exchange
    }

    @Override
    void userStatusChange(UserInfo userInfo) {
        UserMsg userMsg = new UserMsg(
                userName: userInfo.userName,
                preStatus: "",
                currentStatus: "",
                createTime: (new Date()).getTime()
        )
        rabbitTemplate.convertAndSend(exchange, ROUTING_KEY_USER_STATUS_CHANGE, userMsg)
    }
}
