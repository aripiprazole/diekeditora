package com.diekeditora.infra.services

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.page.map
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.RoleAuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.shared.logger
import graphql.relay.Connection
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthorityServiceImpl(
    val repository: AuthorityRepository,
    val userAuthorityRepository: UserAuthorityRepository,
    val roleAuthorityRepository: RoleAuthorityRepository,
) : AuthorityService {
    private val log by logger()

    override suspend fun findAllAuthorities(first: Int, after: String?): Connection<String> {
        return repository.findAll(first, after).map { it.value }.also {
            log.trace("Successfully found all authorities %s", it)
        }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return repository.findAllByUser(user, first, after).map { it.value }.also {
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
        userAuthorityRepository.link(user, authorities.map(::Authority))

        log.trace("Successfully linked %s authorities to user %s", authorities, user)
    }

    @Transactional
    override suspend fun linkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepository.link(role, authorities.map(::Authority))

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepository.unlink(role, authorities.map(::Authority))

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(user: User, authorities: Set<String>) {
        userAuthorityRepository.unlink(user, authorities.map(::Authority))

        log.trace("Successfully unlinked %s authorities from user %s", authorities, user)
    }
}
