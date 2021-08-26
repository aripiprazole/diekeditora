package com.diekeditora.app.tests.config

import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3TestConfig {
    @Bean
    @Primary
    fun s3AsyncClient(): S3AsyncClient = mockk()

    @Bean
    @Primary
    fun s3Client(): S3Client = mockk()
}
