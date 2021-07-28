package com.diekeditora.infra.user

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.AppPage
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserService
import com.diekeditora.infra.utils.assertPageSize
import com.diekeditora.shared.generateRandomString
import com.diekeditora.shared.logger
import com.google.firebase.auth.FirebaseToken
import graphql.relay.Connection
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
internal class UserServiceImpl(val repo: UserRepo) : UserService {
    private val log by logger()

    @Transactional
    override suspend fun findUsers(first: Int, after: String?): Connection<User> {
        assertPageSize(first)

        val items = if (after != null) {
            repo.findAll(first, after).toList()
        } else {
            repo.findAll(first).toList()
        }

        val totalItems = repo.totalEntries()

        val firstIndex = items.firstOrNull()?.let { repo.index(it.username) }
        val lastIndex = items.lastOrNull()?.let { repo.index(it.username) }

        return AppPage.of(totalItems, items, first, firstIndex, lastIndex)
    }

    override suspend fun findUserById(id: UniqueId): User? {
        return repo.findById(id.toUUID())
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
    override suspend fun deleteUser(user: User): User {
        return repo.save(user.copy(deletedAt = LocalDateTime.now())).also {
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
