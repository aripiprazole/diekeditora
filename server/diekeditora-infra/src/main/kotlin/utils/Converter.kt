package com.diekeditora.infra.utils

import io.r2dbc.spi.Row
import org.springframework.data.r2dbc.convert.R2dbcConverter

internal inline fun <reified T> R2dbcConverter.read(row: Row): T {
    return read(T::class.java, row)
}

internal inline fun <reified T> R2dbcConverter.read(): (Row) -> T {
    return { read(it) }
}
