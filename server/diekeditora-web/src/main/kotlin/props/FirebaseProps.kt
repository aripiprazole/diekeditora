package com.diekeditora.web.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "firebase")
class FirebaseProps {
    var googleApplicationCredentials: String? = null
}
