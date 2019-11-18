package io.ryanluoxu.xapp.service.impl

import freemarker.template.Configuration
import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.entity.Mail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import javax.mail.internet.MimeMessage

/**
 * @author luoxu on 11/19
 */
@CompileStatic
@Service
class MailBaseService {
    @Autowired
    JavaMailSender mailSender
    @Autowired
    Configuration freemarkerConfig

    @PostConstruct
    void init(){
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates")
    }

    protected void send(Mail mail){
        MimeMessage message = mailSender.createMimeMessage()
        MimeMessageHelper messageHelper = new MimeMessageHelper(message)
        messageHelper.setTo(mail.mailTo)
        messageHelper.setSubject(mail.subject)
        messageHelper.setText(mail.content, true)
        mailSender.send(message)
    }

}
