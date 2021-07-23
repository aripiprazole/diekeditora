package com.diekeditora.infra.redis

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@OptIn(ExperimentalTime::class)
val expiresIn: Duration = 15.minutes
