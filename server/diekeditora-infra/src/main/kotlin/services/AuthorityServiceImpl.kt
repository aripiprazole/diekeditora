package com.diekeditora.infra.services

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.user.User
import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.shared.logger
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthorityServiceImpl(val repository: AuthorityRepository) : AuthorityService {
    private val log by logger()

    @Transactional
    override suspend fun findAllAuthorities(): Set<String> {
        return repository.findAll().map { it.value }.toSet().also {
            log.trace("Successfully found all authorities %s", it)
        }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(user: User): Set<String> {
        return repository.findAllByUser(user).map { it.value }.toSet().also {
            log.trace("Successfully found all user %s authorities %s", user, it)
        }
    }
}
