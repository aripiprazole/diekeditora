package com.diekeditora.infra.services

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.authority.RoleService
import com.diekeditora.domain.page.Page
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.shared.logger
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
internal class RoleServiceImpl(val repository: RoleRepository) : RoleService {
    private val log by logger()

    override suspend fun findPaginatedRoles(page: Int, pageSize: Int): Page<Role> {
        val roles = repository.findPaginated(page, pageSize).toList()

        return Page.of(roles, pageSize, page, repository.estimateTotalRoles()).also {
            log.trace("Successfully found page of role %d", page)
        }
    }

    override suspend fun findRoleByName(name: String): Role? {
        return repository.findByName(name).also {
            log.trace("Successfully found role by %s by its name", it)
        }
    }

    override suspend fun save(role: Role): Role {
        val target = role.copy(createdAt = LocalDateTime.now())

        return repository.save(target).also {
            log.trace("Successfully saved role %s into database", it)
        }
    }

    override suspend fun update(target: Role, role: Role): Role {
        return repository.save(role.copy(updatedAt = LocalDateTime.now())).also {
            log.trace("Successfully updated role %s", it)
        }
    }

    override suspend fun delete(role: Role) {
        repository.delete(role)

        log.trace("Successfully deleted role %s", role)
    }
}
