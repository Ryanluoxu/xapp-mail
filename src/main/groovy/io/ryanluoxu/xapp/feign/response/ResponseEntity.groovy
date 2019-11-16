package io.ryanluoxu.xapp.feign.response

import groovy.transform.CompileStatic

@CompileStatic
class ResponseEntity<T> {
    static final SUCCESS = "SUCCESS"
    String code
    String message
    T data
    Object extra
}
