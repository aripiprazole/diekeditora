package com.diekeditora.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
@ComponentScan("com.diekeditora")
class DiekEditoraApp

fun main(args: Array<String>) {
    runApplication<DiekEditoraApp>(args = args)
}
