package com.diekeditora.shared.infra

private const val MIN_PAGE_SIZE = 1
private const val MAX_PAGE_SIZE = 50

data class PaginationArg<Receiver : Any, Subject : Any>(
    val receiver: Receiver,
    val first: Int,
    val after: Subject?,
)

fun <R : Any, S : Any> R.toPaginationArg(first: Int, after: S?): PaginationArg<R, S> {
    return PaginationArg(this, first, after)
}

fun assertPageSize(n: Int) {
    require(n <= MAX_PAGE_SIZE) { "The page must be less than $MAX_PAGE_SIZE" }
    require(n >= MIN_PAGE_SIZE) { "The page must be greater than $MIN_PAGE_SIZE" }
}
