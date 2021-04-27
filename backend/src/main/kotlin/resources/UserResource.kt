package com.lorenzoog.diekeditora.resources

import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.services.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserResource(val userService: UserService) {
    @GetMapping
    @ApiParam("The target page", name = "page")
    @ApiOperation("Find all users in the page requested", response = Flow::class)
    @ApiResponses(ApiResponse(code = 200, message = "All users in the page requested"))
    suspend fun index(@RequestParam(defaultValue = "1") page: Int): Flow<User> {
        return userService.findPaginated(page)
    }

    @GetMapping("/:username")
    suspend fun show(username: String): ResponseEntity<User> {
        val user = userService.findByUsername(username)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(user)
    }

    @PostMapping
    suspend fun store(@RequestBody user: User): ResponseEntity<Unit> {
        userService.save(user)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/:username")
    @PutMapping("/:username")
    suspend fun update(username: String, @RequestBody user: User): ResponseEntity<Unit> {
        userService.findByUsername(username)
        userService.save(user)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/:username")
    suspend fun destroy(username: String): ResponseEntity<Unit> {
        val user = userService.findByUsername(username)
            ?: return ResponseEntity.notFound().build()

        userService.destroy(user)

        return ResponseEntity.noContent().build()
    }
}
