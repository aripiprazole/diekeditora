package com.diekeditora.id.infra

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.id.domain.UniqueIdService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
internal class UUIDService : UniqueIdService {
    override fun generateUniqueId(): UniqueId {
        return UniqueId(UUID.randomUUID().toString())
    }
}
