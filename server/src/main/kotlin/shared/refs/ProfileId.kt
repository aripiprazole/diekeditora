package com.diekeditora.shared.refs

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class ProfileId : RefId<UUID>() {
    class Persisted(override val value: UUID) : ProfileId()

    object New : ProfileId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
