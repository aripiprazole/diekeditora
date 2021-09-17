package com.diekeditora.utils

import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.Option
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties

fun R2dbcProperties.toOptions(): ConnectionFactoryOptions {
    return ConnectionFactoryOptions.parse(url)
        .mutate()
        .option(ConnectionFactoryOptions.USER, username)
        .option(ConnectionFactoryOptions.PASSWORD, password)
        .option(ConnectionFactoryOptions.DATABASE, name)
        .also { builder ->
            properties.forEach { (name, value) ->
                builder.option(Option.valueOf(name), value)
            }
        }
        .build()
}
