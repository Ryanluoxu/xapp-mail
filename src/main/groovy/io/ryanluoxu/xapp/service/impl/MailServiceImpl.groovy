package io.ryanluoxu.xapp.service.impl

import freemarker.template.Configuration
import freemarker.template.Template
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.ryanluoxu.xapp.bean.Mail
import io.ryanluoxu.xapp.config.MailProperties
import io.ryanluoxu.xapp.service.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils

import javax.annotation.PostConstruct
import javax.mail.internet.MimeMessage
import java.nio.charset.StandardCharsets

/**
 * @author luoxu on 11/19
 */
@CompileStatic
@Service
@Slf4j
class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender sender
    @Autowired
    Configuration freemarkerConfig
    @Autowired
    MailProperties mailProperties

    @PostConstruct
    void init() {
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates")
    }

    @Override
    void send(Mail mail) {

        MimeMessage message = sender.createMimeMessage()
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name())
            // add attachment
            mail.attachments.each { fileName ->
                helper.addAttachment(fileName, new ClassPathResource(fileName))
            }

            Template template = freemarkerConfig.getTemplate("email-template.ftl")
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.model)

            helper.setFrom(mailProperties.username, mailProperties.fromName)
            helper.setTo(mail.mailTo)
            helper.setSubject(mail.subject)
            helper.setText(html, true)
            sender.send(message)

        } catch (Exception ex) {
            log.error(ex.message)
        }
    }
}
