package com.diekeditora.infra.services

import com.diekeditora.domain.page.Page
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.shared.generateRandomString
import com.diekeditora.shared.logger
import com.google.firebase.auth.FirebaseToken
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    private val log by logger()

    @Transactional
    override suspend fun findUserByUsername(username: String): User? {
        return userRepository.findByUsername(username).also {
            log.trace("Successfully found user by %s by its username", it)
        }
    }

    @Transactional
    override suspend fun findOrCreateUserByToken(token: FirebaseToken): User {
        return userRepository.findByEmail(token.email)
            ?: save(
                User(name = token.name, email = token.email, username = generateUsername(token))
            )
    }

    @Transactional
    override suspend fun findPaginatedUsers(page: Int, pageSize: Int): Page<User> {
        val users = userRepository.findPaginated(page, pageSize).toList()

        return Page.of(users, pageSize, page, userRepository.estimateTotalUsers()).also {
            log.trace("Successfully found page of user %d", page)
        }
    }

    @Transactional
    override suspend fun update(target: User, user: User): User {
        return userRepository.save(target.update(user)).also {
            log.trace("Successfully updated user %s", user)
        }
    }

    @Transactional
    override suspend fun save(user: User): User {
        val target = user.copy(createdAt = LocalDateTime.now())

        return userRepository.save(target).also {
            log.trace("Successfully saved user %s into database", user)
        }
    }

    @Transactional
    override suspend fun delete(user: User): User {
        return userRepository.save(user.copy(deletedAt = LocalDateTime.now())).also {
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
