package com.diekeditora.database.domain

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class ChapterId : RefId<UUID>() {
    class Persisted(override val value: UUID) : ChapterId()

    object New : ChapterId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
