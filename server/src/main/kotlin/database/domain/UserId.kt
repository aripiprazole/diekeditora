package com.diekeditora.database.domain

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class UserId : RefId<UUID>() {
    class Persisted(override val value: UUID) : UserId()

    object New : UserId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
