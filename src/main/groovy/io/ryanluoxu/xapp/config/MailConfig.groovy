package io.ryanluoxu.xapp.config

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

/**
 * @Author luoxu on 18/11/19
 */
@CompileStatic
@Configuration
class MailConfig {
    @Autowired
    MailProperties mailProperties

    @Bean
    JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl()
        mailSender.setHost(mailProperties.host)
        mailSender.setPort(Integer.parseInt(mailProperties.port))
        mailSender.setUsername(mailProperties.username)
        mailSender.setPassword(mailProperties.password)

        Properties javaMailProperties = new Properties()
        javaMailProperties.put("mail.smtp.starttls.enable","ture")
        javaMailProperties.put("mail.smtp.auth","ture")
        javaMailProperties.put("mail.transport.protocol","smtp")
        javaMailProperties.put("mail.debug","ture")

        mailSender.setJavaMailProperties(javaMailProperties)
        return mailSender
    }
}
