package com.diekeditora.infra.redis

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface CachedService {
    val expiresIn: Duration
}
