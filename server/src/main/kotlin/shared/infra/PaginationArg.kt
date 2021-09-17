package com.diekeditora.shared.infra

data class PaginationArg<Receiver : Any, Subject : Any>(
    val receiver: Receiver,
    val first: Int,
    val after: Subject?,
)

fun <R : Any, S : Any> R.toPaginationArg(first: Int, after: S?): PaginationArg<R, S> {
    return PaginationArg(this, first, after)
}
