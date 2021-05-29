package com.lorenzoog.diekeditora.web.resources

import com.lorenzoog.diekeditora.domain.page.Page
import com.lorenzoog.diekeditora.domain.user.User
import com.lorenzoog.diekeditora.domain.user.UserService
import com.lorenzoog.diekeditora.web.utils.toResponseEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/users")
class UserResource(val userService: UserService) {
    @GetMapping
    suspend fun index(@RequestParam(defaultValue = "1") page: Int): Page<User> {
        return userService.findPaginatedUsers(page)
    }

    @GetMapping("{username}")
    suspend fun show(@PathVariable username: String): ResponseEntity<User> {
        val user = userService.findUserByUsername(username) ?: return userNotFound()

        return user.toResponseEntity()
    }

    @PostMapping
    suspend fun store(@RequestBody body: User): ResponseEntity<User> {
        val user = userService.save(body)
        val uri = URI("/users/${user.username}")

        return ResponseEntity.created(uri).body(user)
    }

    @RequestMapping("{username}")
    @PutMapping
    @PatchMapping
    suspend fun update(
        @PathVariable username: String,
        @RequestBody user: User
    ): ResponseEntity<Unit> {
        userService.updateUserByUsername(username, user) ?: return userNotFound()

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("{username}")
    suspend fun destroy(@PathVariable username: String): ResponseEntity<Unit> {
        userService.findUserByUsername(username)
            ?.also { userService.delete(it) }
            ?: return userNotFound()

        return ResponseEntity.noContent().build()
    }

    private fun <T> userNotFound(): ResponseEntity<T> {
        return ResponseEntity.notFound().build()
    }
}
