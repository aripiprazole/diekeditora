package com.diekeditora.shared.infra

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Runs [ApiFuture] in current [kotlinx.coroutines.CoroutineScope] and returns
 * the result
 *
 * @return [T]
 */
suspend fun <T> ApiFuture<T>.await(): T = coroutineScope {
    suspendCoroutine { cont ->
        val callback = object : ApiFutureCallback<T> {
            override fun onFailure(ex: Throwable) = cont.resumeWithException(ex)
            override fun onSuccess(result: T) = cont.resume(result)
        }

        ApiFutures.addCallback(this@await, callback) { fn ->
            launch { fn.run() }
        }
    }
}
