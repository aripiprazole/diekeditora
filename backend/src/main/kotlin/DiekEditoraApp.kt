package com.lorenzoog.diekeditora

import com.lorenzoog.diekeditora.utils.getLogger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DiekEditoraApp

private val log = getLogger<DiekEditoraApp>()

fun main(args: Array<String>) {
    runCatching {
        runApplication<DiekEditoraApp>(args = args)
    }.onFailure {
        log.error("Unexpected exception", it)
    }
}
