package com.diekeditora.web.tests.resources

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.page.Page
import com.diekeditora.domain.user.User
import com.diekeditora.domain.user.UserAddAuthorityDto
import com.diekeditora.domain.user.UserAddRoleDto
import com.diekeditora.domain.user.UserInput
import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import com.diekeditora.web.tests.factories.AuthorityFactory
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserResourceTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userRoleRepository: UserRoleRepository,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val authorityRepository: AuthorityRepository,
    @Autowired val userAuthorityRepository: UserAuthorityRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val authorityFactory: AuthorityFactory,
    @Autowired val userFactory: UserFactory,
    @Autowired val client: WebTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should not retrieve paginated users without authorities`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        client.mutateWith(auth.configure())
            .get().uri("/users?page=1")
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(userFactory.create())

        val pageNumber = 1
        val pageSize = 15
        val users = userRepository.findPaginated(pageNumber, pageSize).toList()

        val page = Page.of(users, pageSize, pageNumber, userRepository.estimateTotalUsers())

        client.mutateWith(auth.configure("user.view"))
            .get().uri("/users?page=$pageNumber")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<Page<User>>()
            .isEqualTo(page)
    }

    @Test
    fun `test should not retrieve an user without authorities`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }

        client.mutateWith(auth.configure())
            .get().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should add user's a role`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create())

        val userRoles = userRoleRepository.findByUser(user).toList()

        client.mutateWith(auth.configure("role.admin"))
            .post().uri("/users/${user.username}/roles")
            .bodyValue(UserAddRoleDto(setOf(role.name)))
            .exchange()
            .expectStatus().isNoContent

        assertEquals(userRoles + role, userRoleRepository.findByUser(user).toList())
    }

    @Test
    fun `test should not add user's a role without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val role = roleRepository.save(roleFactory.create())

        client.mutateWith(auth.configure("role.admin"))
            .post().uri("/users/${user.username}/roles")
            .bodyValue(UserAddRoleDto(setOf(role.name)))
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should add user's an authority`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create()).value

        val userAuthorities = userAuthorityRepository.findByUser(user).toList().map { it.value }

        client.mutateWith(auth.configure("authority.admin"))
            .post().uri("/users/${user.username}/authorities")
            .bodyValue(UserAddAuthorityDto(setOf(authority)))
            .exchange()
            .expectStatus().isNoContent

        assertEquals(
            userAuthorities + authority,
            userAuthorityRepository.findByUser(user).toList().map { it.value }
        )
    }

    @Test
    fun `test should not add user's an authority without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())
        val authority = authorityRepository.save(authorityFactory.create()).value

        client.mutateWith(auth.configure("authority.admin"))
            .post().uri("/users/${user.username}/authorities")
            .bodyValue(UserAddAuthorityDto(setOf(authority)))
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should retrieve user's roles`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userRoleRepository.save(it, roleFactory.createMany(5))
        }

        client.mutateWith(auth.configure("role.view"))
            .get().uri("/users/${user.username}/roles")
            .exchange()
            .expectStatus().isOk
            .expectBody<Flow<Role>>()
            .isEqualTo(userRoleRepository.findByUser(user))
    }

    @Test
    fun `test should not retrieve user's roles without authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        client.mutateWith(auth.configure())
            .get().uri("/users/${user.username}/roles")
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should retrieve user's authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userAuthorityRepository.save(it, authorityFactory.createMany(5))
        }

        client.mutateWith(auth.configure("authority.view"))
            .get().uri("/users/${user.username}/authorities")
            .exchange()
            .expectStatus().isOk
            .expectBody<Flow<String>>()
            .isEqualTo(userAuthorityRepository.findByUser(user).map { it.value })
    }

    @Test
    fun `test should not retrieve user's authorities`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create())

        client.mutateWith(auth.configure())
            .get().uri("/users/${user.username}/authorities")
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should retrieve an user`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }

        client.mutateWith(auth.configure("user.view"))
            .get().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<User>()
            .isEqualTo(
                requireNotNull(userRepository.findByUsername(user.username)).also {
                    assertNull(it.updatedAt)
                    assertNull(it.deletedAt)
                }
            )
    }

    @Test
    fun `test should not store an user without authorities`(): Unit = runBlocking {
        val value = UserInput.from(userFactory.create())

        client.mutateWith(auth.configure())
            .post().uri("/users")
            .bodyValue(value)
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should store an user`(): Unit = runBlocking {
        val value = UserInput.from(userFactory.create())

        val exchange =
            client.mutateWith(auth.configure("user.store"))
                .post().uri("/users")
                .bodyValue(value)
                .exchange()
                .expectStatus().isCreated
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody<User>()
                .returnResult()

        val body = requireNotNull(exchange.responseBody) { "User username must be not null" }
        val user = requireNotNull(userRepository.findByUsername(body.username)) {
            "User must be not null"
        }

        assertEquals(user.username, body.username)
        assertEquals(user.name, body.name)
        assertEquals(user.birthday, body.birthday)
        assertNull(user.updatedAt)
        assertNull(user.deletedAt)
    }

    @Test
    fun `test should not update an user by requests body without authorities`(): Unit =
        runBlocking {
            val user = userFactory.create().let { userRepository.save(it) }
            val newUser = userFactory.create()

            client.mutateWith(auth.configure())
                .patch().uri("/users/${user.username}")
                .bodyValue(UserInput.from(newUser))
                .exchange()
                .expectStatus().isForbidden
        }

    @Test
    fun `test should update an user by requests body`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }
        val newUser = userFactory.create()
        val id = requireNotNull(user.id) { "User's id must be not null" }

        client.mutateWith(auth.configure("user.update"))
            .patch().uri("/users/${user.username}")
            .bodyValue(UserInput.from(newUser))
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertEquals(newUser.username, user.username)
        assertEquals(newUser.name, user.name)
        assertEquals(newUser.birthday, user.birthday)
        assertNotNull(user.updatedAt)
        assertNull(user.deletedAt)
    }

    @Test
    fun `test should not delete an user by its username without authorities`(): Unit = runBlocking {
        val user = userFactory.create().let { userRepository.save(it) }

        client.mutateWith(auth.configure())
            .delete().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should delete an user by its username`(): Unit = runBlocking {
        var user = userFactory.create().let { userRepository.save(it) }

        val id = requireNotNull(user.id) { "User's id must be not null" }

        client.mutateWith(auth.configure("user.destroy"))
            .delete().uri("/users/${user.username}")
            .exchange()
            .expectStatus().isNoContent

        user = requireNotNull(userRepository.findById(id)) { "User must be not null" }

        assertNotNull(user.deletedAt)
    }
}
