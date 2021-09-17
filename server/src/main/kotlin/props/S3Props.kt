package com.diekeditora.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "s3")
class S3Props {
    var enabled: Boolean = true
    var bucketName: String = "diekeditora"
    var endpoint: String? = null
    var accessKey: String? = null
    var accessSecret: String? = null
    var region: String? = null
}
