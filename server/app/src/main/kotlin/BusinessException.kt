package com.diekeditora

class BusinessException(
    override val message: String,
    override val cause: Exception? = null
) : Exception()
