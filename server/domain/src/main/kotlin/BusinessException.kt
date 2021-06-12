package com.diekeditora.domain

class BusinessException(
    override val message: String,
    override val cause: Exception? = null
) : Exception()
