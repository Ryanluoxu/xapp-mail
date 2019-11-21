package io.ryanluoxu.xapp.config

import groovy.transform.CompileStatic
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @Author luoxu on 18/11/19
 */
@CompileStatic
@Component
@ConfigurationProperties("spring.mail")
class MailProperties {
    String host
    String port
    String username
    String fromName
    String password
}
