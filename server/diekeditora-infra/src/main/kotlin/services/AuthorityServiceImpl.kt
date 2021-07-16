package com.diekeditora.infra.services

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.page.map
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.repositories.AuthorityRepo
import com.diekeditora.infra.repositories.RoleAuthorityRepo
import com.diekeditora.infra.repositories.UserAuthorityRepo
import com.diekeditora.shared.logger
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class AuthorityServiceImpl(
    val repo: AuthorityRepo,
    val userAuthorityRepo: UserAuthorityRepo,
    val roleAuthorityRepo: RoleAuthorityRepo,
) : AuthorityService {
    private val log by logger()

    @Transactional
    override suspend fun findAllAuthorities(first: Int, after: String?): Connection<String> {
        require(first > 1) { "The size of page must be bigger than 1" }
        require(first < 50) { "The size of page must be less than 50" }

        val authorities = if (after != null) {
            repo.findAll(first, after).toList()
        } else {
            repo.findAll(first).toList()
        }

        val totalItems = repo.estimateTotalAuthorities()

        val firstIndex = authorities.firstOrNull()?.let { repo.findIndex(it.value) }
        val lastIndex = authorities.lastOrNull()?.let { repo.findIndex(it.value) }

        return AppPage
            .of(totalItems, authorities, first, firstIndex, lastIndex)
            .map { it.value }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(user: User): Set<String> {
        return userAuthorityRepo.findAllByUser(user).map { it.value }.toSet().also {
            log.trace("Successfully found all user %s authorities %s", user, it)
        }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return userAuthorityRepo.findAllByUser(user, first, after).map { it.value }.also {
            log.trace("Successfully found all user %s authorities %s", user, it)
        }
    }

    @Transactional
    override suspend fun findAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return userAuthorityRepo.findByUser(user, first, after).map { it.value }.also {
            log.trace("Successfully found user authorities %s by user", it)
        }
    }

    @Transactional
    override suspend fun findAuthoritiesByRole(
        role: Role,
        first: Int,
        after: String?
    ): Connection<String> {
        return roleAuthorityRepo.findByRole(role, first, after).map { it.value }.also {
            log.trace("Successfully found role authorities %s by role", it)
        }
    }

    @Transactional
    override suspend fun linkAuthorities(user: User, authorities: Set<String>) {
        userAuthorityRepo.link(user, authorities.map { Authority.of(it) })

        log.trace("Successfully linked %s authorities to user %s", authorities, user)
    }

    @Transactional
    override suspend fun linkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepo.link(role, authorities.map { Authority.of(it) })

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepo.unlink(role, authorities.map { Authority.of(it) })

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(user: User, authorities: Set<String>) {
        userAuthorityRepo.unlink(user, authorities.map { Authority.of(it) })

        log.trace("Successfully unlinked %s authorities from user %s", authorities, user)
    }
}
