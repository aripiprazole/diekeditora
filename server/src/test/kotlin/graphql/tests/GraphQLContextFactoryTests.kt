package com.diekeditora.tests.graphql.tests

import com.diekeditora.graphql.infra.AuthGraphQLContextFactory
import com.diekeditora.tests.factories.UserFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class GraphQLContextFactoryTests(@Autowired val userFactory: UserFactory) {

    @Test
    fun `test should authenticate successfully with bearer token`(): Unit = runBlocking {
        val converter = mockk<ServerAuthenticationConverter>()
        val manager = mockk<ReactiveAuthenticationManager>()

        val factory = AuthGraphQLContextFactory(converter, manager)

        val request = mockk<ServerRequest> {
            every { exchange() } returns mockk()
        }

        val user = userFactory.create()

        val authentication = UsernamePasswordAuthenticationToken(user, null, emptyList())

        every { converter.convert(any()) } returns Mono.just(authentication)
        every { manager.authenticate(any()) } returns Mono.just(authentication)

        val context = factory.generateContext(request)

        assertNotNull(context)
        assertEquals(authentication, context.authentication)
        assertEquals(user, user)
    }

    @Test
    fun `test should authenticate successfully with context holder`(): Unit = runBlocking {
        val converter = mockk<ServerAuthenticationConverter>()
        val manager = mockk<ReactiveAuthenticationManager>()

        val factory = AuthGraphQLContextFactory(converter, manager)

        val user = userFactory.create()
        val authentication = UsernamePasswordAuthenticationToken(user, null, emptyList())

        Flux.just(mockk<ServerRequest>())
            .flatMap { request -> mono { factory.generateContext(request) } }
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
            .subscribe { context ->
                assertNotNull(context)
                assertEquals(authentication, context.authentication)
                assertEquals(user, user)
            }
    }
}
