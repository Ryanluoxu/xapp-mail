package io.ryanluoxu.xapp.controller

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.bean.Mail
import io.ryanluoxu.xapp.service.impl.MailServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @Author luoxu on 11/19
 */
@CompileStatic
@RestController
class MailController {

    @Autowired
    MailServiceImpl mailBaseService

    @GetMapping("send")
    void send() {
        Mail mail = new Mail()
        mail.mailTo = "luoxu2011@gmail.com"
        mail.subject = "Test Email"
        mail.attachments = ["java.jpg"]
        Map<String, Object> model = [:]
        model["name"] = "Ryan"
        mail.setModel(model)
        mailBaseService.send(mail)
    }
}
