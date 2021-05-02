package com.lorenzoog.diekeditora.error

import com.lorenzoog.diekeditora.utils.get
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.core.annotation.MergedAnnotation
import org.springframework.core.annotation.MergedAnnotations
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {
    @OptIn(ExperimentalStdlibApi::class)
    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions) = buildMap<String, Any> {
        val error = getError(request)

        val annotations = MergedAnnotations
            .from(error.javaClass, SearchStrategy.TYPE_HIERARCHY)
            .get<ResponseStatus>()

        val status = error.determineStatus(annotations)

        put("status", status.value())
        put("message", error.determineMessage(annotations) ?: status.reasonPhrase)
    }

    private fun Throwable.determineMessage(annotations: MergedAnnotation<ResponseStatus>): String? {
        return when (this) {
            is ResponseStatusException -> reason
            is BindingResult -> message
            else -> annotations.get<String>("reason")
        }
    }

    private fun Throwable.determineStatus(annotations: MergedAnnotation<ResponseStatus>): HttpStatus {
        return when (this) {
            is ResponseStatusException -> status
            else -> annotations.get<HttpStatus>("code") ?: HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
