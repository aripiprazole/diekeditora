package com.diekeditora.web.tests.auth

import com.diekeditora.domain.user.UserService
import com.diekeditora.shared.generateRandomString
import com.diekeditora.web.security.AuthenticationManager
import com.diekeditora.web.tests.factories.TokenFactory
import com.diekeditora.web.tests.factories.UserFactory
import com.google.api.core.ApiFutures
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
class AuthenticationManagerTests(
    @Autowired val userFactory: UserFactory,
    @Autowired val tokenFactory: TokenFactory,
) {
    @Test
    fun `test should authenticate successfully with bearer token`(): Unit = runBlocking {
        val auth = mockk<FirebaseAuth>()
        val userService = mockk<UserService>()

        var authentication: Authentication =
            BearerTokenAuthenticationToken(generateRandomString(15))

        val manager = AuthenticationManager(auth, userService)

        val user = userFactory.create()
        val token = tokenFactory.create()

        every { auth.verifyIdTokenAsync(any()) } returns ApiFutures.immediateFuture(token)
        coEvery { userService.findOrCreateUserByToken(any()) } returns user

        authentication = manager.authenticate(authentication).awaitSingle()

        assertTrue(authentication is UsernamePasswordAuthenticationToken)
        assertEquals(user, authentication.principal)
        assertEquals(token, authentication.credentials)
    }
}
