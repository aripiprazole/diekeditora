package com.diekeditora.lib

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.responseType
import io.ktor.util.InternalAPI
import io.ktor.util.pipeline.PipelineContext
import kotlin.reflect.full.declaredMemberExtensionFunctions

class KtorHandlerScope : HandlerScope {
  override suspend fun guard(permission: String) {
    println("check if user has permission $permission")
  }
}

@OptIn(InternalAPI::class)
suspend fun <Req, T : Req> PipelineContext<Unit, ApplicationCall>.handle(handler: Handler<Req>, req: T) {
  val handlerClass = handler::class
  val memberExtensionFunctions = handlerClass.declaredMemberExtensionFunctions
  val handleFunction = memberExtensionFunctions.find { it.name == "handle" }!!

  call.response.responseType = handleFunction.returnType

  when (val response = handler.run { KtorHandlerScope().handle(req) }) {
    null -> call.response.pipeline.execute(call, "")
    else -> call.response.pipeline.execute(call, response)
  }
}
