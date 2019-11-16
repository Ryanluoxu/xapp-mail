package io.ryanluoxu.xapp.exception

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.enums.ResponseCodeEnum

@CompileStatic
class CustomException extends RuntimeException {
    String code

    CustomException(ResponseCodeEnum responseCodeEnum){
        super(responseCodeEnum.message)
        this.code = responseCodeEnum.code
    }
    CustomException(ResponseCodeEnum responseCodeEnum, String message){
        super(message)
        this.code = responseCodeEnum.code
    }

}
