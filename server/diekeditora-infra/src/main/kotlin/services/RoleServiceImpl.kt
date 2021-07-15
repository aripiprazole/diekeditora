package com.diekeditora.infra.services

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.domain.user.User
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import com.diekeditora.shared.logger
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
internal class RoleServiceImpl(
    val repository: RoleRepository,
    val userRoleRepository: UserRoleRepository,
) : RoleService {
    private val log by logger()

    @Transactional
    override suspend fun findRoles(first: Int, after: String?): Connection<Role> {
        require(first > 1) { "The size of page must be bigger than 1" }
        require(first < 50) { "The size of page must be less than 50" }

        val roles = if (after != null) {
            repository.findAll(first, after).toList()
        } else {
            repository.findAll(first).toList()
        }

        val totalItems = repository.estimateTotalRoles()

        val firstIndex = roles.firstOrNull()?.let { repository.findIndex(it.name) }
        val lastIndex = roles.lastOrNull()?.let { repository.findIndex(it.name) }

        return AppPage.of(totalItems, roles, first, firstIndex, lastIndex)
    }

    @Transactional
    override suspend fun findRoleByName(name: String): Role? {
        return repository.findByName(name)?.also {
            log.trace("Successfully found role by %s by its name", it)
        }
    }

    @Transactional
    override suspend fun findRolesByUser(user: User, first: Int, after: String?): Connection<Role> {
        return userRoleRepository.findByUser(user).also {
            log.trace("Successfully found user roles %s by user", it)
        }
    }

    @Transactional
    override suspend fun linkRoles(user: User, roles: Set<Role>) {
        userRoleRepository.link(user, roles)

        log.trace("Successfully linked %s roles to user %s", roles, user)
    }

    @Transactional
    override suspend fun unlinkRoles(user: User, roles: Set<Role>) {
        userRoleRepository.unlink(user, roles)

        log.trace("Successfully unlinked %s roles to user %s", roles, user)
    }

    @Transactional
    override suspend fun saveRole(role: Role): Role {
        val target = role.copy(createdAt = LocalDateTime.now())

        return repository.save(target).also {
            log.trace("Successfully saved role %s into database", it)
        }
    }

    @Transactional
    override suspend fun updateRole(target: Role, role: Role): Role {
        return repository.save(role.copy(updatedAt = LocalDateTime.now())).also {
            log.trace("Successfully updated role %s", it)
        }
    }

    @Transactional
    override suspend fun deleteRole(role: Role) {
        repository.delete(role)

        log.trace("Successfully deleted role %s", role)
    }
}
