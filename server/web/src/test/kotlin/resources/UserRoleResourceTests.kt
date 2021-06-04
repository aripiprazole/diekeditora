package com.diekeditora.web.tests.resources

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.user.UserAddRoleDto
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.diekeditora.web.tests.utils.AuthenticationMocker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import kotlin.test.assertEquals

@SpringBootTest
class UserRoleResourceTests(
    @Autowired val userRepository: UserRepository,
    @Autowired val userFactory: UserFactory,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val userRoleRepository: UserRoleRepository,
    @Autowired val client: WebTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
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
    fun `test should retrieve user's roles`(): Unit = runBlocking {
        val user = userRepository.save(userFactory.create()).also {
            userRoleRepository.save(it, roleRepository.saveAll(roleFactory.createMany(5)).toList())
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
}
