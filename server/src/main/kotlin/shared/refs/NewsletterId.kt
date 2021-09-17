package com.diekeditora.shared.refs

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class NewsletterId : RefId<UUID>() {
    class Persisted(override val value: UUID) : NewsletterId()

    object New : NewsletterId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
