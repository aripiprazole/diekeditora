package com.lorenzoog.diekeditora.resources

import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloResource {
    @GetMapping("/hello")
    @ApiOperation("Sends a hello, world", response = String::class)
    @ApiResponses(ApiResponse(code = 200, message = "Hello, world"))
    @Suppress("FunctionOnlyReturningConstant")
    suspend fun hello(): String = "Hello, world!"
}
