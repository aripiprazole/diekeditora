package com.diekeditora.web.resources

import com.diekeditora.domain.user.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SessionResource {
    @GetMapping("/session")
    @CrossOrigin
    suspend fun session(@AuthenticationPrincipal principal: User): User {
        return principal
    }
}
