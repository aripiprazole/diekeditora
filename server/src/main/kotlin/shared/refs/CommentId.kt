package com.diekeditora.shared.refs

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class CommentId : RefId<UUID>() {
    class Persisted(override val value: UUID) : CommentId()

    object New : CommentId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
