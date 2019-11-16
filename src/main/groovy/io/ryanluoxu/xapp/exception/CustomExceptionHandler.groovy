package io.ryanluoxu.xapp.exception

import groovy.transform.CompileStatic
import io.ryanluoxu.xapp.bean.ResponseBean
import io.ryanluoxu.xapp.enums.ResponseCodeEnum
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import java.nio.file.AccessDeniedException

@CompileStatic
@ControllerAdvice
class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException)
    static ResponseBean customExceptionHandler(CustomException ex){
        return new ResponseBean(ex.code, ex.message)
    }

    @ExceptionHandler(AccessDeniedException)
    static ResponseBean accessDeniedExceptionHandler(AccessDeniedException ex){
        return new ResponseBean(ResponseCodeEnum.UNAUTHORIZED)
    }

}
