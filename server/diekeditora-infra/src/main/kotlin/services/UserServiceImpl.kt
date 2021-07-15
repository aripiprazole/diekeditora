package com.diekeditora.infra.services

import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.shared.generateRandomString
import com.diekeditora.shared.logger
import com.google.firebase.auth.FirebaseToken
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
internal class UserServiceImpl(val repository: UserRepository) : UserService {
    private val log by logger()

    @Transactional
    override suspend fun findUsers(first: Int, after: String?): Connection<User> {
        require(first > 1) { "The size of page must be bigger than 1" }
        require(first < 50) { "The size of page must be less than 50" }

        val users = if (after != null) {
            repository.findAll(first, after).toList()
        } else {
            repository.findAll(first).toList()
        }

        val totalItems = repository.estimateTotalUsers()

        val firstIndex = users.firstOrNull()?.let { repository.findIndex(it.username) }
        val lastIndex = users.lastOrNull()?.let { repository.findIndex(it.username) }

        return AppPage.of(totalItems, users, first, firstIndex, lastIndex)
    }

    @Transactional
    override suspend fun findUserByUsername(username: String): User? {
        return repository.findByUsername(username).also {
            log.trace("Successfully found user by %s by its username", it)
        }
    }

    @Transactional
    override suspend fun findOrCreateUserByToken(token: FirebaseToken): User {
        return repository.findByEmail(token.email)
            ?: saveUser(
                User(name = token.name, email = token.email, username = generateUsername(token))
            )
    }

    @Transactional
    override suspend fun updateUser(target: User, user: User): User {
        return repository.save(target.update(user)).also {
            log.trace("Successfully updated user %s", user)
        }
    }

    @Transactional
    override suspend fun saveUser(user: User): User {
        val target = user.copy(createdAt = LocalDateTime.now())

        return repository.save(target).also {
            log.trace("Successfully saved user %s into database", user)
        }
    }

    @Transactional
    override suspend fun deleteUser(user: User): User {
        return repository.save(user.copy(deletedAt = LocalDateTime.now())).also {
            log.trace("Successfully deleted %s", it)
        }
    }

    private fun generateUsername(token: FirebaseToken): String {
        return token.name.substring(0, NAME_START_SIZE) + "_" + generateRandomString(NAME_END_SIZE)
    }

    companion object {
        private const val NAME_START_SIZE = 12
        private const val NAME_END_SIZE = 4
    }
}
