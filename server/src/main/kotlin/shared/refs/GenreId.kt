package com.diekeditora.shared.refs

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class GenreId : RefId<UUID>() {
    class Persisted(override val value: UUID) : GenreId()

    object New : GenreId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
