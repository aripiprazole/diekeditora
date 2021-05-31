package com.diekeditora.domain.session

import kotlinx.serialization.Serializable

@Serializable
enum class SessionProvider {
    Google, Facebook, Github, Twitter, Jwt;

    companion object {
        fun of(name: String?): SessionProvider {
            TODO()
        }
    }
}
