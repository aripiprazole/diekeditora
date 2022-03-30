package com.diekeditora.lib

interface Handler<Req> {
  suspend fun HandlerScope.handle(request: Req): Any?
}

@Suppress("UNCHECKED_CAST")
fun <Req> Handler<out Req>.fix(): Handler<Req> {
  return this as Handler<Req>
}

interface HandlerScope {
  suspend fun guard(permission: String)
}
