package com.lorenzoog.diekeditora.web.config

import com.lorenzoog.diekeditora.infra.repositories.UserRepository
import com.lorenzoog.diekeditora.infra.services.AuthenticationServiceImpl
import com.lorenzoog.diekeditora.infra.services.SessionServiceImpl
import com.lorenzoog.diekeditora.infra.services.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {
    @Bean
    fun userService(userRepository: UserRepository) = UserServiceImpl(userRepository)

    @Bean
    fun sessionService() = SessionServiceImpl()

    @Bean
    fun authenticationService() = AuthenticationServiceImpl()
}
