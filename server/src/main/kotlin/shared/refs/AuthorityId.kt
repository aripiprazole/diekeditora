package com.diekeditora.shared.refs

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class AuthorityId : RefId<UUID>() {
    class Persisted(override val value: UUID) : AuthorityId()

    object New : AuthorityId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
