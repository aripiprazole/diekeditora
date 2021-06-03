package com.diekeditora.web.tests.resources

import com.diekeditora.domain.authority.Role
import com.diekeditora.domain.page.Page
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.web.tests.factories.RoleFactory
import com.diekeditora.web.tests.utils.AuthenticationMocker
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
class RoleResourceTests(
    @Autowired val roleRepository: RoleRepository,
    @Autowired val roleFactory: RoleFactory,
    @Autowired val client: WebTestClient,
    @Autowired val auth: AuthenticationMocker,
) {
    @Test
    fun `test should retrieve paginated roles`(): Unit = runBlocking {
        roleRepository.save(roleFactory.create())

        val pageNumber = 1
        val pageSize = 15
        val roles = roleRepository.findPaginated(pageNumber, pageSize).toList()

        val page = Page.of(roles, pageSize, pageNumber, roleRepository.estimateTotalRoles())

        client.mutateWith(auth.configure("role.view"))
            .get().uri("/roles?page=$pageNumber")
            .exchange()
            .expectStatus().isOk
            .expectBody<Page<Role>>()
            .isEqualTo(page)
    }

    @Test
    fun `test should not retrieve paginated roles without authorities`(): Unit = runBlocking {
        roleRepository.save(roleFactory.create())

        client.mutateWith(auth.configure())
            .get().uri("/roles?page=1")
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should store a role`(): Unit = runBlocking {
        var role = roleFactory.create()

        val exchange =
            client.mutateWith(auth.configure("role.store"))
                .post().uri("/roles")
                .bodyValue(role)
                .exchange()
                .expectStatus().isCreated
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody<Role>()
                .returnResult()

        val body = requireNotNull(exchange.responseBody) { "Body must be not null" }

        role = requireNotNull(roleRepository.findByName(body.name)) {
            "Role must be not null"
        }

        assertEquals(role.name, body.name)
        assertEquals(role.createdAt, body.createdAt)
        assertEquals(role.updatedAt, body.updatedAt)
        assertNull(role.updatedAt)
    }

    @Test
    fun `test should not store a role without authorities`(): Unit = runBlocking {
        client.mutateWith(auth.configure())
            .post().uri("/roles")
            .bodyValue(roleFactory.create())
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should update a role by requests body`(): Unit = runBlocking {
        var role = roleFactory.create().let { roleRepository.save(it) }
        val newRole = roleFactory.create()
        val id = requireNotNull(role.id) { "Role's id must be not null" }

        client.mutateWith(auth.configure("role.update"))
            .patch().uri("/roles/${role.name}")
            .bodyValue(newRole)
            .exchange()
            .expectStatus().isNoContent

        role = requireNotNull(roleRepository.findById(id)) { "Role must be not null" }

        assertEquals(newRole.name, role.name)
        assertEquals(newRole.authorities, role.authorities)
        assertEquals(newRole.updatedAt, role.updatedAt)
        assertEquals(newRole.createdAt, role.createdAt)
        assertNotNull(role.updatedAt)
    }

    @Test
    fun `test should not update a role by requests body without authorities`(): Unit = runBlocking {
        val role = roleFactory.create().let { roleRepository.save(it) }
        val newRole = roleFactory.create()

        client.mutateWith(auth.configure())
            .patch().uri("/roles/${role.name}")
            .bodyValue(newRole)
            .exchange()
            .expectStatus().isForbidden
    }

    @Test
    fun `test should delete an user by its username`(): Unit = runBlocking {
        val role = roleFactory.create().let { roleRepository.save(it) }
        val id = requireNotNull(role.id) { "Role's id must be not null" }

        client.mutateWith(auth.configure("role.destroy"))
            .delete().uri("/roles/${role.name}")
            .exchange()
            .expectStatus().isNoContent

        assertNull(roleRepository.findById(id))
    }

    @Test
    fun `test should not delete an user by its username without authorities`(): Unit = runBlocking {
        val role = roleFactory.create().let { roleRepository.save(it) }

        client.mutateWith(auth.configure())
            .delete().uri("/roles/${role.name}")
            .exchange()
            .expectStatus().isForbidden
    }
}
