package com.diekeditora.infra.repositories

import com.diekeditora.infra.entities.Authority
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AuthorityRepository : CoroutineSortingRepository<Authority, UUID>
