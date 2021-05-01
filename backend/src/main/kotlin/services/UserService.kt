package com.lorenzoog.diekeditora.services

import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.repositories.UserRepository
import com.lorenzoog.diekeditora.utils.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(val repository: UserRepository) {
    private val log by logger()

    suspend fun delete(user: User) {
        repository.save(user.copy(deletedAt = LocalDateTime.now()))

        log.trace("Successfully deleted user with id ${user.id}")
    }

    suspend fun findPaginated(page: Int): Flow<User> {
        return repository.findAll(page).also {
            log.trace("Successfully retrieved ${it.count()} users in page $page")
        }
    }

    suspend fun findByUsername(username: String): User? {
        return repository.findByUsername(username)?.also { (id) ->
            log.trace("Successfully retrieved user with id $id by username $username")
        }
    }

    suspend fun save(user: User): User {
        return repository.save(user).also { (id, username) ->
            if (user.id != null) {
                log.trace("Successfully updated user with id $id by username $username")
            } else {
                log.trace("Successfully created user with id $id")
            }
        }
    }
}
