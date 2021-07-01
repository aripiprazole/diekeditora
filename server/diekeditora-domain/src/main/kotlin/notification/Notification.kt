package com.diekeditora.domain.notification

import com.diekeditora.domain.id.UniqueId

sealed class Notification {
    abstract val id: UniqueId
    abstract val rawContent: String
}
