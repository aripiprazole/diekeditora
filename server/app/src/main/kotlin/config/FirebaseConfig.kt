package com.diekeditora.app.config

import com.diekeditora.infra.props.FirebaseProps
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class FirebaseConfig(val props: FirebaseProps) {
    @Bean
    @ConditionalOnMissingBean
    fun app(): FirebaseApp? {
        if (!props.enabled) return null

        val googleApplicationCredentials = requireNotNull(props.googleApplicationCredentials) {
            "Google application credentials should not be null when firebase is enabled"
        }

        val inputStream = File(googleApplicationCredentials).inputStream()

        return FirebaseApp.initializeApp(
            FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun auth(app: FirebaseApp? = null): FirebaseAuth? {
        return when {
            !props.enabled -> null
            props.googleApplicationCredentials == null -> null
            app == null -> null
            else -> FirebaseAuth.getInstance(app)
        }
    }
}
