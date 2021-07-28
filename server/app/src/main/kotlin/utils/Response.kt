package com.diekeditora.web.utils

import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait

fun <T : Any> T.toResponseEntity(): ResponseEntity<T> {
    return ResponseEntity.ok(this)
}

suspend inline fun <reified T : Any> Flow<T>.toServerResponse(): ServerResponse {
    return ServerResponse.ok().bodyAndAwait(this)
}

suspend fun <T : Any> T.toServerResponse(): ServerResponse {
    return ServerResponse.ok().bodyValueAndAwait(this)
}
