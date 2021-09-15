package com.diekeditora.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "firebase")
class FirebaseProps {
    var enabled: Boolean = true
    var googleApplicationCredentials: String? = null
}
