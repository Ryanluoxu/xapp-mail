package io.ryanluoxu.xapp.util

import groovy.transform.CompileStatic

@CompileStatic
class IdGenerator {
    static String generate(String prefix){
        return prefix + UUID.randomUUID().toString().toUpperCase()
    }
}
