package com.diekeditora.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
@EnableR2dbcRepositories("com.diekeditora.infra")
@ComponentScan("com.diekeditora.domain", "com.diekeditora.web")
class DiekEditoraApp

fun main(args: Array<String>) {
    runApplication<DiekEditoraApp>(args = args)
}
