package com.diekeditora.infra.authority

import com.diekeditora.domain.authority.AuthorityService
import com.diekeditora.domain.page.map
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import com.diekeditora.infra.repo.findAllAsConnection
import com.diekeditora.shared.logger
import graphql.relay.Connection
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
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
        return repo.findAllAsConnection(first, after).map { it.value }
    }

    @Transactional
    override suspend fun findAllAuthoritiesByUser(user: User): Set<String> {
        return userAuthorityRepo.findAllByUser(user).map { it.value }.toSet()
    }

    @Transactional
    override suspend fun findAuthoritiesByUser(
        user: User,
        first: Int,
        after: String?
    ): Connection<String> {
        return userAuthorityRepo
            .findAllAsConnection(first, after, owner = user.id?.toUUID())
            .map(Authority::value)
    }

    @Transactional
    override suspend fun findAuthoritiesByRole(
        role: Role,
        first: Int,
        after: String?
    ): Connection<String> {
        return roleAuthorityRepo
            .findAllAsConnection(first, after, owner = role.id?.toUUID())
            .map(Authority::value)
    }

    @Transactional
    override suspend fun linkAuthorities(user: User, authorities: Set<String>) {
        requireNotNull(user.id) { "User id must be not null" }

        authorities
            .filter { repo.findByValue(it) == null }
            .map { repo.save(Authority.of(it)) }
            .forEach loop@{ authority ->
                val authorityId = requireNotNull(authority.id) { "Authority id must be not null" }

                userAuthorityRepo.link(user, authorityId)
            }

        log.trace("Successfully linked %s authorities to user %s", authorities, user)
    }

    @Transactional
    override suspend fun linkAuthorities(role: Role, authorities: Set<String>) {
        requireNotNull(role.id) { "Role id must be not null" }

        authorities
            .filter { repo.findByValue(it) == null }
            .map { repo.save(Authority.of(it)) }
            .forEach loop@{ authority ->
                val authorityId = requireNotNull(authority.id) { "Authority id must be not null" }

                roleAuthorityRepo.link(role, authorityId)
            }

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(role: Role, authorities: Set<String>) {
        roleAuthorityRepo.unlink(role, authorities)

        log.trace("Successfully linked %s authorities to role %s", authorities, role)
    }

    @Transactional
    override suspend fun unlinkAuthorities(user: User, authorities: Set<String>) {
        userAuthorityRepo.unlink(user, authorities)

        log.trace("Successfully unlinked %s authorities from user %s", authorities, user)
    }
}
