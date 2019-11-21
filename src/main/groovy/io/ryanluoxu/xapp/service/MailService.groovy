package io.ryanluoxu.xapp.service

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.bean.Mail

@CompileStatic
interface MailService {
    void send(Mail mail)
}
