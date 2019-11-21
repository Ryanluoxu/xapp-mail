package io.ryanluoxu.xapp.bean

import groovy.transform.CompileStatic

/**
 * @Author luoxu on 18/11/19
 */
@CompileStatic
class Mail {
    String mailTo
    String mailCc
    String mailBcc
    String subject
    /**
     * resources/java.jpg
     * [java.jpg]
     */
    List<String> attachments
    Map<String, Object> model
    Mail(){
        attachments = []
    }
}
