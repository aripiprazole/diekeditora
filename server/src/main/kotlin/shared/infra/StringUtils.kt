package com.diekeditora.shared.infra

import kotlin.random.Random

private val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun generateRandomString(length: Int): String {
    return (1..length)
        .map { Random.nextInt(0, chars.size) }
        .map { chars[it] }
        .joinToString("")
}
