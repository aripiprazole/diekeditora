package com.diekeditora.web.config

import com.diekeditora.web.props.FirebaseProps
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class FirebaseConfig(val props: FirebaseProps) {
    @Bean
    fun app(): FirebaseApp? {
        val googleApplicationCredentials = props.googleApplicationCredentials ?: return null

        val inputStream = File(googleApplicationCredentials).inputStream()

        return FirebaseApp.initializeApp(
            FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
        )
    }

    @Bean
    @Suppress("Detekt.ReturnCount")
    fun auth(app: FirebaseApp? = null): FirebaseAuth? {
        return when {
            props.googleApplicationCredentials == null -> null
            app == null -> null
            else -> FirebaseAuth.getInstance(app)
        }
    }
}
