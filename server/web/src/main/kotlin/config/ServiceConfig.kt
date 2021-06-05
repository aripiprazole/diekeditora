package com.diekeditora.web.config

import com.diekeditora.infra.repositories.AuthorityRepository
import com.diekeditora.infra.repositories.RoleRepository
import com.diekeditora.infra.repositories.UserRepository
import com.diekeditora.infra.services.AuthorityServiceImpl
import com.diekeditora.infra.services.RoleServiceImpl
import com.diekeditora.infra.services.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {
    @Bean
    fun userService(repository: UserRepository) = UserServiceImpl(repository)

    @Bean
    fun roleService(repository: RoleRepository) = RoleServiceImpl(repository)

    @Bean
    fun authorityService(repository: AuthorityRepository) = AuthorityServiceImpl(repository)
}
