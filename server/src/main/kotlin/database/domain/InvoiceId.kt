package com.diekeditora.database.domain

import com.diekeditora.id.domain.IdNotPersistedDelegate
import com.diekeditora.id.domain.RefId
import java.util.UUID

sealed class InvoiceId : RefId<UUID>() {
    class Persisted(override val value: UUID) : InvoiceId()

    object New : InvoiceId() {
        override val value by IdNotPersistedDelegate<UUID>()
    }
}
