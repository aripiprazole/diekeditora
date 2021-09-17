package com.diekeditora.database.domain

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class NotificationId : RefId<UUID>() {
    class Persisted(override val value: UUID) : NotificationId()

    object New : NotificationId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
