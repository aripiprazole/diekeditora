package com.diekeditora.web.config

import com.diekeditora.infra.repositories.RoleAuthorityRepository
import com.diekeditora.infra.repositories.UserAuthorityRepository
import com.diekeditora.infra.repositories.UserRoleRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

@Configuration
class RepositoryConfig(val template: R2dbcEntityTemplate) {
    @Bean
    fun userRoleRepository(): UserRoleRepository = UserRoleRepository(template)

    @Bean
    fun userAuthorityRepository(): UserAuthorityRepository = UserAuthorityRepository(template)

    @Bean
    fun roleAuthorityRepository(): RoleAuthorityRepository = RoleAuthorityRepository(template)
}
