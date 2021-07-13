package com.diekeditora.infra.services

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.role.RoleService
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.repositories.RoleAuthorityRepository
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.shared.logger
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
internal class RoleServiceImpl(
    val repository: RoleRepository,
    val roleAuthorityRepository: RoleAuthorityRepository,
) : RoleService {
    private val log by logger()

    @Transactional
    override suspend fun findPaginatedRoles(page: Int, pageSize: Int): AppPage<Role> {
        val roles = repository.findPaginated(page, pageSize).toList()

        return AppPage.of(roles, pageSize, page, repository.estimateTotalRoles()).also {
            log.trace("Successfully found page of role %d", page)
        }
    }

    @Transactional
    override suspend fun findRoleByName(name: String): Role? {
        return repository.findByName(name)?.also {
            log.trace("Successfully found role by %s by its name", it)
        }
    }

    @Transactional
    override suspend fun findRoleAuthorities(role: Role): Set<String> {
        return roleAuthorityRepository.findByRole(role).map { it.value }.toSet().also {
            log.trace("Successfully found role authorities %s by role", it)
        }
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
