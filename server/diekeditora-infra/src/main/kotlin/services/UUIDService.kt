package com.diekeditora.infra.services

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.id.UniqueIdService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
internal class UUIDService : UniqueIdService {
    override fun generateUniqueId(): UniqueId {
        return UniqueId(UUID.randomUUID().toString())
    }
}
