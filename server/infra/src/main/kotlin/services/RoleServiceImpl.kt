package com.diekeditora.infra.services

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.authority.RoleService
import com.diekeditora.domain.page.Page
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.shared.logger
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RoleServiceImpl(val roleRepository: RoleRepository) : RoleService {
    private val log by logger()

    @Transactional
    override suspend fun findPaginatedRoles(page: Int, pageSize: Int): Page<Role> {
        val roles = roleRepository.findPaginated(page, pageSize).toList()

        return Page.of(roles, pageSize, page, roleRepository.estimateTotalRoles()).also {
            log.trace("Successfully found page of role %d", page)
        }
    }

    @Transactional
    override suspend fun findRoleByName(name: String): Role? {
        return roleRepository.findByName(name).also {
            log.trace("Successfully found role by %s by its name", it)
        }
    }

    @Transactional
    override suspend fun save(role: Role): Role {
        val target = role.copy(createdAt = LocalDateTime.now())

        return roleRepository.save(target).also {
            log.trace("Successfully saved role %s into database", it)
        }
    }

    @Transactional
    override suspend fun update(target: Role, role: Role): Role {
        return roleRepository.save(role.copy(updatedAt = LocalDateTime.now())).also {
            log.trace("Successfully updated role %s", it)
        }
    }

    @Transactional
    override suspend fun delete(role: Role) {
        roleRepository.delete(role)

        log.trace("Successfully deleted role %s", role)
    }
}
