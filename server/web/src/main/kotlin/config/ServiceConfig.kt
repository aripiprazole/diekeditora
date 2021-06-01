package com.diekeditora.web.config

import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.services.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {
    @Bean
    fun userService(userRepository: UserRepository) = UserServiceImpl(userRepository)
}
