package io.ryanluoxu.xapp.enums

enum ResponseCodeEnum {

    SUCCESS("SUCCESS")
    , ERROR("ERROR")
    , UNAUTHORIZED("Not authorized to access.")

    private static String message
    private ResponseCodeEnum(String message){
        this.message = message
    }
    static String getMessage(){
        return message
    }
    String getCode(){
        name()
    }
}
