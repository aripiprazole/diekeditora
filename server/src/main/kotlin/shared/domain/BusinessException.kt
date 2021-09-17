package com.diekeditora.shared.domain

class BusinessException(
    override val message: String,
    override val cause: Exception? = null
) : Exception()
