package com.diekeditora.web.resources

import com.diekeditora.domain.session.Session
import com.diekeditora.domain.session.SessionService
import com.diekeditora.domain.user.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/session")
class SessionResource(val sessionService: SessionService) {
    @GetMapping
    fun session(@AuthenticationPrincipal session: Session): User {
        return session.user
    }

    @GetMapping("/validate")
    suspend fun validate(@RequestParam code: String, @RequestParam provider: String?): OAuth2AccessTokenResponse {
        TODO()
    }
}
