package com.lorenzoog.diekeditora.resources

import com.lorenzoog.diekeditora.entities.User
import com.lorenzoog.diekeditora.services.UserService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
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
import java.time.LocalDateTime

@RestController
@RequestMapping("/users")
class UserResource(val userService: UserService) {
    @GetMapping
    @ApiParam("The target page", name = "page")
    @ApiOperation("Finds all users in the specified page", response = Flow::class)
    @ApiResponses(ApiResponse(code = 200, message = "All users in the page requested"))
    suspend fun index(@RequestParam(defaultValue = "1") page: Int): Flow<User> {
        return userService.findPaginated(page)
    }

    @GetMapping("/{username}")
    @ApiParam("The target user's username", name = "username")
    @ApiOperation("Finds details of a specific user by it's username", response = User::class)
    @ApiResponses(
        ApiResponse(code = 200, message = "Details of a specific user"),
        ApiResponse(code = 404, message = "Requested entity not found")
    )
    suspend fun show(@PathVariable username: String): ResponseEntity<User> {
        val user = userService.findByUsername(username)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(user)
    }

    @PostMapping
    @ApiOperation("Creates a new user with request body")
    @ApiResponses(
        ApiResponse(code = 201, message = "Details of a specific user"),
        ApiResponse(code = 409, message = "Already exists an user with the requested data"),
    )
    suspend fun store(@RequestBody user: User): ResponseEntity<User> {
        userService.save(user.copy(emailVerifiedAt = LocalDateTime.now()))

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(user)
    }

    @RequestMapping("{username}")
    @PutMapping
    @PatchMapping
    @ApiParam("The target user's username", name = "username")
    @ApiOperation("Updates the target user by it's username with request's body")
    @ApiResponses(
        ApiResponse(code = 204, message = "Successfully updated user the target's user"),
        ApiResponse(code = 409, message = "Already exists an user with the requested data"),
        ApiResponse(code = 404, message = "Could not find the target's user"),
    )
    suspend fun update(@PathVariable username: String, @RequestBody body: User): ResponseEntity<Unit> {
        val user = userService.findByUsername(username)
            ?: return ResponseEntity.notFound().build()

        userService.save(body.copy(id = user.id, updatedAt = LocalDateTime.now()))

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("{username}")
    @ApiParam("The target user's username", name = "username")
    @ApiOperation("Delete the target user by it's username")
    @ApiResponses(
        ApiResponse(code = 204, message = "Successfully deleted the target's user"),
        ApiResponse(code = 404, message = "Could not find the target's user"),
    )
    suspend fun destroy(@PathVariable username: String): ResponseEntity<Unit> {
        val user = userService.findByUsername(username)
            ?: return ResponseEntity.notFound().build()

        userService.delete(user)

        return ResponseEntity.noContent().build()
    }
}
