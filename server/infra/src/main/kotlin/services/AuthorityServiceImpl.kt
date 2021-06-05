package com.diekeditora.infra.services

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.shared.logger
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthorityServiceImpl(val authorityRepository: AuthorityRepository) : AuthorityService {
    private val log by logger()

    @Transactional
    override suspend fun findAllAuthorities(): Set<String> {
        return authorityRepository.findAll().map { it.value }.toSet().also {
            log.trace("Successfully found all authorities %s", it)
        }
    }

    @Transactional
    override suspend fun createAuthorities(vararg authorities: String): Set<String> {
        return authorityRepository
            .saveAll(authorities.map { Authority.of(it) })
            .map(Authority::value)
            .toSet()
            .also {
                log.trace("Successfully saved authorities %s into database", authorities.toList())
            }
    }

    @Transactional
    override suspend fun deleteAuthorities(vararg authorities: String) {
        authorityRepository.deleteAll(authorities.map { Authority.of(it) })

        log.trace("Successfully deleted authorities %s from database", authorities.toList())
    }
}
