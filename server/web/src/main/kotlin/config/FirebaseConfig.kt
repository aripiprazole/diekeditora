package com.diekeditora.web.config

import com.diekeditora.shared.tryOrNull
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfig {
    @Bean
    fun app(): FirebaseApp? = tryOrNull {
        FirebaseApp.initializeApp(
            FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()
        )
    }

    @Bean
    fun auth(app: FirebaseApp): FirebaseAuth? = tryOrNull {
        FirebaseAuth.getInstance(app)
    }
}
