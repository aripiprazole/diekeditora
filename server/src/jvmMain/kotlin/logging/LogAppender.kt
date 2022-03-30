package com.diekeditora.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.encoder.EncoderBase
import java.time.Instant
import java.time.format.DateTimeFormatter

private const val LINE_SEPARATOR = "\r\n"

private const val LOG_FORMAT =
  "$GRAY[$LIGHT_RED%s $RESET%s $LIGHT_GRAY%s $RESET%s$GRAY]$RESET %s$RESET$LINE_SEPARATOR"

private const val LEVEL_FORMAT = "%s%-5s$RESET"

class LogAppender : ConsoleAppender<ILoggingEvent>() {
  init {
    encoder = LogEncoder()
  }
}

private class LogEncoder : EncoderBase<ILoggingEvent>() {
  override fun headerBytes() = byteArrayOf()
  override fun footerBytes() = byteArrayOf()

  override fun encode(event: ILoggingEvent): ByteArray {
    val time = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    val level = getLevelString(event.level)

    val name = event.loggerName.split(".").let { it[it.size - 1] }
    val thread = event.threadName

    val message = when (val cause = event.throwableProxy) {
      null -> event.message + RESET
      else -> {
        val causeMessage = "${cause.className}: ${cause.message}"

        buildString {
          append(LINE_SEPARATOR)
          append(RED)
          append(causeMessage)
          append(LINE_SEPARATOR)

          for (element in cause.stackTraceElementProxyArray.orEmpty()) {
            append("\t$RED$element$LINE_SEPARATOR")
          }

          append(RESET)
        }
      }
    }

    return LOG_FORMAT.format(time, level, name, thread, message).toByteArray()
  }
}

private fun getLevelString(level: Level): String = when (val levelInt = level.levelInt) {
  Level.ERROR_INT -> LEVEL_FORMAT.format(RED, "ERROR")
  Level.WARN_INT -> LEVEL_FORMAT.format(YELLOW, "WARN")
  Level.INFO_INT -> LEVEL_FORMAT.format(LIGHT_GREEN, "INFO")
  Level.DEBUG_INT -> LEVEL_FORMAT.format(BLUE, "DEBUG")
  Level.TRACE_INT -> LEVEL_FORMAT.format(LIGHT_BLUE, "TRACE")
  else -> LEVEL_FORMAT.format(WHITE, "$levelInt")
}
