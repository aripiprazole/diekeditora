package com.diekeditora.database.domain

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class MangaId : RefId<UUID>() {
    class Persisted(override val value: UUID) : MangaId()

    object New : MangaId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
