package com.lorenzoog.diekeditora

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DiekEditoraApp

private val log = LoggerFactory.getLogger(DiekEditoraApp::class.java)

fun main(args: Array<String>) {
    runCatching {
        runApplication<DiekEditoraApp>(*args)
    }.onFailure {
        log.error("Unexpected exception", it)
    }
}
