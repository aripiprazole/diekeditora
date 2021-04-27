package com.lorenzoog.diekeditora.services

import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.repositories.UserRepository
import com.lorenzoog.diekeditora.utils.getLogger
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class UserService(val repository: UserRepository) {
    private val log = getLogger<UserService>()

    suspend fun destroy(user: User) {
        repository.delete(user)
    }

    suspend fun findPaginated(page: Int): Flow<User> {
        return repository.findAll()
    }

    suspend fun findByUsername(username: String): User? {
        return repository.findByUsername(username)
    }

    suspend fun save(user: User): User {
        return repository.save(user)
    }
}
