package com.diekeditora.web.config

import com.diekeditora.infra.props.S3Props
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config(val props: S3Props) {
    val credentialsProvider = AwsCredentialsProvider {
        val accessKey = requireNotNull(props.accessKey) {
            "S3 Access key should not be null when s3 is enabled"
        }

        val accessSecret = requireNotNull(props.accessSecret) {
            "S3 Access secret should not be null when s3 is enabled"
        }

        AwsBasicCredentials.create(accessKey, accessSecret)
    }

    @Bean
    @ConditionalOnMissingBean
    fun s3AsyncClient(): S3AsyncClient? {
        if (!props.enabled) return null

        return S3AsyncClient.builder()
            .credentialsProvider(credentialsProvider)
            .region(Region.of(validateRegion()))
            .build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun s3Client(): S3Client? {
        if (!props.enabled) return null

        return S3Client.builder()
            .credentialsProvider(credentialsProvider)
            .region(Region.of(validateRegion()))
            .build()
    }

    private fun validateRegion(): String {
        return requireNotNull(props.region) {
            "S3 region should not be null when s3 is enabled"
        }
    }
}
