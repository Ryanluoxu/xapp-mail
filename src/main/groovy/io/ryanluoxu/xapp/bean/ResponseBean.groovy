package io.ryanluoxu.xapp.bean

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.enums.ResponseCodeEnum
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@CompileStatic
class ResponseBean extends ResponseEntity {

    ResponseBean(HttpStatus status, String code, String message, Object data){
        super(["code":code, "message":message, "data":data],status)
    }

    ResponseBean(String code, String message, Object data) {
        super(HttpStatus.OK)
    }

    ResponseBean(String code, String message) {
        this(HttpStatus.OK, code, message, null)
    }

    ResponseBean(ResponseCodeEnum responseCodeEnum) {
        this(responseCodeEnum.code, responseCodeEnum.message)
    }

    static success(){
        return new ResponseBean(ResponseCodeEnum.SUCCESS.code, ResponseCodeEnum.SUCCESS.message)
    }
    static success(Object data){
        return new ResponseBean(ResponseCodeEnum.SUCCESS.code, ResponseCodeEnum.SUCCESS.message, data)
    }

}
