package com.diekeditora.graphql

import com.expediagroup.graphql.server.types.GraphQLResponse
import com.expediagroup.graphql.server.types.GraphQLServerError

fun <T, R> GraphQLResponse<T>.copy(
  data: R?,
  errors: List<GraphQLServerError>? = this.errors,
  extensions: Map<Any, Any?>? = this.extensions,
): GraphQLResponse<R> {
  return GraphQLResponse(data, errors, extensions)
}
