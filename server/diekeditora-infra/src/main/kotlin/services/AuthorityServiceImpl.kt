package com.diekeditora.infra.services

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.page.map
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.RoleAuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.shared.logger
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthorityServiceImpl(
    val repository: AuthorityRepository,
    val userAuthorityRepository: UserAuthorityRepository,
    val roleAuthorityRepository: RoleAuthorityRepository,
) : AuthorityService {
    private val log by logger()

    @Transactional
    override suspend fun findAllAuthorities(first: Int, after: String?): Connection<String> {
        require(first > 1) { "The size of page must be bigger than 1" }
        require(first < 50) { "The size of page must be less than 50" }

        val authorities = if (after != null) {
            repository.findAll(first, after).toList()
        } else {
            repository.findAll(first).toList()
        }

        val totalItems = repository.estimateTotalAuthorities()

        val firstIndex = authorities.firstOrNull()?.let { repository.findIndex(it.value) }
        val lastIndex = authorities.lastOrNull()?.let { repository.findIndex(it.value) }

        return AppPage
            .of(totalItems, authorities, first, firstIndex, lastIndex)
            .map { it.value }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(user: User): Set<String> {
        return userAuthorityRepository.findAllByUser(user).map { it.value }.toSet().also {
            log.trace("Successfully found all user %s authorities %s", user, it)
        }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return userAuthorityRepository.findAllByUser(user, first, after).map { it.value }.also {
            log.trace("Successfully found all user %s authorities %s", user, it)
        }
    }

    @Transactional
    override suspend fun findAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return userAuthorityRepository.findByUser(user, first, after).map { it.value }.also {
            log.trace("Successfully found user authorities %s by user", it)
        }
    }

    @Transactional
    override suspend fun findAuthoritiesByRole(
        role: Role,
        first: Int,
        after: String?
    ): Connection<String> {
        return roleAuthorityRepository.findByRole(role, first, after).map { it.value }.also {
            log.trace("Successfully found role authorities %s by role", it)
        }
    }

    @Transactional
    override suspend fun linkAuthorities(user: User, authorities: Set<String>) {
        userAuthorityRepository.link(user, authorities.map { Authority.of(it) })

        log.trace("Successfully linked %s authorities to user %s", authorities, user)
    }

    @Transactional
    override suspend fun linkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepository.link(role, authorities.map { Authority.of(it) })

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepository.unlink(role, authorities.map { Authority.of(it) })

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(user: User, authorities: Set<String>) {
        userAuthorityRepository.unlink(user, authorities.map { Authority.of(it) })

        log.trace("Successfully unlinked %s authorities from user %s", authorities, user)
    }
}
