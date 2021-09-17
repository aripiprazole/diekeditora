package com.diekeditora.utils

private const val MIN_PAGE_SIZE = 1
private const val MAX_PAGE_SIZE = 50

fun assertPageSize(n: Int) {
    require(n <= MAX_PAGE_SIZE) { "The page must be less than $MAX_PAGE_SIZE" }
    require(n >= MIN_PAGE_SIZE) { "The page must be greater than $MIN_PAGE_SIZE" }
}
