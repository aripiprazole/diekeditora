@file:Suppress("unused")

package com.diekeditora.app.utils

import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.AuthorizeExchangeDsl
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers

fun AuthorizeExchangeDsl.get(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.GET, *paths)
}

fun AuthorizeExchangeDsl.put(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.PUT, *paths)
}

fun AuthorizeExchangeDsl.options(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.OPTIONS, *paths)
}

fun AuthorizeExchangeDsl.patch(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.PATCH, *paths)
}

fun AuthorizeExchangeDsl.post(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.POST, *paths)
}

fun AuthorizeExchangeDsl.head(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.HEAD, *paths)
}

fun AuthorizeExchangeDsl.trace(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.TRACE, *paths)
}

fun AuthorizeExchangeDsl.delete(vararg paths: String): ServerWebExchangeMatcher {
    return pathMatchers(HttpMethod.DELETE, *paths)
}
