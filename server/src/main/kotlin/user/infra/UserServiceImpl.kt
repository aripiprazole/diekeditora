package com.diekeditora.user.infra

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.shared.infra.findAllAsConnection
import com.diekeditora.shared.infra.generateRandomString
import com.diekeditora.shared.infra.logger
import com.diekeditora.user.domain.User
import com.diekeditora.user.domain.UserService
import com.google.firebase.auth.FirebaseToken
import graphql.relay.Connection
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserServiceImpl(val repo: UserRepo) : UserService {
    private val log by logger()

    @Transactional
    override suspend fun findUsers(first: Int, after: String?): Connection<User> {
        return repo.findAllAsConnection(first, after, Sort.by(ASC, "created_at"))
    }

    override suspend fun findUserById(id: UniqueId): User? {
        return repo.findById(id)
    }

    @Transactional
    override suspend fun findUserByUsername(username: String): User? {
        return repo.findByUsername(username).also {
            log.trace("Successfully found user by %s by its username", it)
        }
    }

    @Transactional
    override suspend fun findOrCreateUserByToken(token: FirebaseToken): User {
        return repo.findByEmail(token.email)
            ?: saveUser(
                User(name = token.name, email = token.email, username = generateUsername(token))
            )
    }

    @Transactional
    override suspend fun updateUser(user: User): User {
        requireNotNull(user.id) { "User id must be not null when updating" }

        return repo.save(user).also {
            log.trace("Successfully updated user %s", user)
        }
    }

    @Transactional
    override suspend fun saveUser(user: User): User {
        val target = user.copy(createdAt = LocalDateTime.now())

        return repo.save(target).also {
            log.trace("Successfully saved user %s into database", user)
        }
    }

    @Transactional
    override suspend fun deleteUser(user: User) {
        repo.deleteById(user.id)

        log.trace("Successfully deleted %s", user)
    }

    private fun generateUsername(token: FirebaseToken): String {
        return token.name.substring(0, NAME_START_SIZE) + "_" + generateRandomString(NAME_END_SIZE)
    }

    companion object {
        private const val NAME_START_SIZE = 12
        private const val NAME_END_SIZE = 4
    }
}
