package io.ryanluoxu.xapp.service

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.entity.UserInfo

@CompileStatic
interface MqProducerService {
    void userStatusChange(UserInfo userInfo)
}
