package com.diekeditora.infra.utils

fun assertPageSize(n: Int) {
    require(n < 50) { "The page must be less than 50" }
    require(n > 1) { "The page must be greater than 1" }
}
