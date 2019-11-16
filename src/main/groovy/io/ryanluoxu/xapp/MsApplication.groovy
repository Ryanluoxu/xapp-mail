package io.ryanluoxu.xapp

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@CompileStatic
@SpringBootApplication
class MsApplication {
    static void main(String[] args) {
        SpringApplication.run(MsApplication, args)
    }
}
