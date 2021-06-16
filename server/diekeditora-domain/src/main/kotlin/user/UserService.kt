package com.diekeditora.domain.user

import com.diekeditora.domain.page.Page
import com.diekeditora.domain.role.Role
import com.google.firebase.auth.FirebaseToken
import org.springframework.stereotype.Service

@Service
interface UserService {
    suspend fun findUserByUsername(username: String): User?

    suspend fun findOrCreateUserByToken(token: FirebaseToken): User

    suspend fun findPaginatedUsers(page: Int = 1, pageSize: Int = 15): Page<User>

    suspend fun findUserRoles(user: User): List<Role>

    suspend fun findUserAuthorities(user: User): List<String>

    suspend fun linkRoles(user: User, roles: List<Role>)

    suspend fun unlinkRoles(user: User, roles: List<Role>)

    suspend fun linkAuthorities(user: User, authorities: List<String>)

    suspend fun unlinkAuthorities(user: User, authorities: List<String>)

    suspend fun update(target: User, user: User): User

    suspend fun save(user: User): User

    suspend fun delete(user: User): User
}
