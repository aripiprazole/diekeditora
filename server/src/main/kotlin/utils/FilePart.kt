package com.diekeditora.utils

import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.http.codec.multipart.Part
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream

suspend fun Part.toByteArray(): ByteArray {
    val list = content()
        .flatMap { Flux.just(it.asByteBuffer().array()) }
        .collectList()
        .awaitFirst()

    return ByteArrayOutputStream()
        .apply { list.forEach(::write) }
        .toByteArray()
}
