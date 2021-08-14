package com.diekeditora.infra.role

import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.infra.repo.findAllAsConnection
import com.diekeditora.shared.logger
import graphql.relay.Connection
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
internal class RoleServiceImpl(
    val repo: RoleRepo,
    val userRoleRepo: UserRoleRepo,
) : RoleService {
    private val log by logger()

    @Transactional
    override suspend fun findRoles(first: Int, after: String?): Connection<Role> {
        return repo.findAllAsConnection(first, after)
    }

    @Transactional
    override suspend fun findRoleByName(name: String): Role? {
        return repo.findByName(name)?.also {
            log.trace("Successfully found role by %s by its name", it)
        }
    }

    @Transactional
    override suspend fun findRolesByUser(user: User, first: Int, after: String?): Connection<Role> {
        return userRoleRepo.findAllAsConnection(first, after, owner = user.id)
    }

    @Transactional
    override suspend fun linkRoles(user: User, roles: Set<Role>) {
        requireNotNull(user.id) { "User id must be not null" }

        roles
            .map { repo.save(it) }
            .forEach loop@{ role ->
                val authorityId = requireNotNull(role.id) { "Role id must be not null" }

                userRoleRepo.link(user, authorityId)
            }

        log.trace("Successfully linked %s roles to user %s", roles, user)
    }

    @Transactional
    override suspend fun unlinkRoles(user: User, roles: Set<Role>) {
        userRoleRepo.unlink(user, roles.mapNotNull { it.id })

        log.trace("Successfully unlinked %s roles to user %s", roles, user)
    }

    @Transactional
    override suspend fun saveRole(role: Role): Role {
        val target = role.copy(createdAt = LocalDateTime.now())

        return repo.save(target).also {
            log.trace("Successfully saved role %s into database", it)
        }
    }

    @Transactional
    override suspend fun updateRole(role: Role): Role {
        requireNotNull(role.id) { "Role id must be not null when updating" }

        return repo.save(role).also {
            log.trace("Successfully updated role %s", it)
        }
    }

    @Transactional
    override suspend fun deleteRole(role: Role) {
        repo.delete(role)

        log.trace("Successfully deleted role %s", role)
    }

    override suspend fun userHasRole(role: Role): Boolean {
        TODO("Not yet implemented")
    }
}
