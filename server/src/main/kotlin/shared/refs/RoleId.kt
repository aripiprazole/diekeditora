package com.diekeditora.shared.refs

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class RoleId : RefId<UUID>() {
    class Persisted(override val value: UUID) : RoleId()

    object New : RoleId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
