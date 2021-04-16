package com.lorenzoog.diekeditora.logger

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.CoreConstants.LINE_SEPARATOR
import ch.qos.logback.core.encoder.EncoderBase
import com.lorenzoog.diekeditora.logger.LogColor.Cyan
import com.lorenzoog.diekeditora.logger.LogColor.Red
import com.lorenzoog.diekeditora.logger.LogColor.Reset
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val FORMAT = "[%s] %s$Reset in$Cyan %s$Reset at $Cyan%s$Reset: %s$Reset$LINE_SEPARATOR"

class LogEncoder(private val dateFormatter: DateTimeFormatter) : EncoderBase<ILoggingEvent>() {
    override fun headerBytes(): ByteArray = byteArrayOf()
    override fun footerBytes(): ByteArray = byteArrayOf()

    override fun encode(event: ILoggingEvent): ByteArray {
        val time = dateFormatter.format(LocalDateTime.now())
        val level = LogLevel.fromLogbackLevel(event.level)

        val name = event.loggerName.split(".").let { it[it.size - 1] }
        val thread = event.threadName + runBlocking coroutineName@{
            val (coroutineName) = currentCoroutineContext()[CoroutineName]
                ?: return@coroutineName ""

            "($coroutineName)"
        }

        val message = if (event.throwableProxy == null) {
            event.message + Reset
        } else {
            event.throwableProxy!!.stackTraceElementProxyArray?.let {
                val cause = event.throwableProxy

                val causeMessage = "${cause?.className}: ${cause?.message}"

                listOf(LINE_SEPARATOR + Red + causeMessage, *it)
                    .joinToString(separator = "") { element ->
                        "\t$Red$element$LINE_SEPARATOR"
                    }
            }
        }

        return FORMAT.format(time, level, name, thread, message).toByteArray()
    }
}
