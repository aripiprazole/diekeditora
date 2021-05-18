package com.lorenzoog.diekeditora.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories("com.lorenzoog.diekeditora.infra")
@ComponentScan("com.lorenzoog.diekeditora.domain", "com.lorenzoog.diekeditora.web")
class DiekEditoraApp

fun main(args: Array<String>) {
    runApplication<DiekEditoraApp>(args = args)
}
