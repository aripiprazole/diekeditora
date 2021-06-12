package com.diekeditora.web.tests.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class FirebaseTestConfig {
    @Bean
    @Primary
    fun credentialsMock(): GoogleCredentials = mockk()

    @Bean
    @Primary
    fun appMock(): FirebaseApp = mockk()

    @Bean
    @Primary
    fun authMock(): FirebaseAuth = mockk()
}
