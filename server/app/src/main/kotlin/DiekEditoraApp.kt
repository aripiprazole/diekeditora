package com.diekeditora.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
@EnableR2dbcRepositories("com.diekeditora")
@ComponentScan("com.diekeditora")
class DiekEditoraApp

fun main(args: Array<String>) {
    runApplication<DiekEditoraApp>(args = args)
}
