package io.ryanluoxu.xapp.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.transform.CompileStatic

/**
 * @author luoxu on 10/19
 */
@CompileStatic
class JsonUtil {
    static final ObjectMapper COMMON_OBJECT_MAPPER
    static {
        COMMON_OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
    }
    static <T> T parse(String jsonString, Class<T> clazz){
        return COMMON_OBJECT_MAPPER.readValue(jsonString, clazz)
    }
    static String toString(Object object){
        return COMMON_OBJECT_MAPPER.writeValueAsString(object)
    }
}
