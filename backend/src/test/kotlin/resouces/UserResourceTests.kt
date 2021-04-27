package com.lorenzoog.diekeditora.resouces

import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.repositories.UserRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureWebTestClient
class UserResourceTests(
    @Autowired val client: WebTestClient,
    @Autowired val userRepository: UserRepository
) {

    @Test
    fun `test should retrieve paginated users`(): Unit = runBlocking {
        userRepository.save(
            User(
                name = "Lorenzo",
                username = "LorenzooG",
                email = "lorenzo@email.com",
                password = "password",
                birthday = LocalDateTime.now()
            )
        )

        client.get().uri("/users", mapOf("page" to 1))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<List<User>>()
            .isEqualTo(userRepository.findAll().toList())
    }
}
