package com.diekeditora.shared.infra

import kotlinx.coroutines.reactive.awaitSingleOrNull
import reactor.core.publisher.Mono

suspend fun <T> Mono<T>.isEmpty(): Boolean {
    return awaitSingleOrNull() != null
}
