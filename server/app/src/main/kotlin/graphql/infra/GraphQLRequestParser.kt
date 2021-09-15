package graphql.infra

import com.diekeditora.shared.toByteArray
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLRequestParser
import com.expediagroup.graphql.server.types.GraphQLBatchRequest
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.MULTIPART_FORM_DATA
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.awaitMultipartData

@Service
@Primary
class GraphQLRequestParser(val objectMapper: ObjectMapper) :
    SpringGraphQLRequestParser(objectMapper) {
    @Suppress("Detekt.ReturnCount")
    override suspend fun parseRequest(request: ServerRequest): GraphQLServerRequest? {
        return when (request.method()) {
            HttpMethod.POST -> {
                val contentType = request.headers().contentType().orElse(APPLICATION_JSON)
                return when {
                    contentType.includes(GRAPHQL_MEDIA_TYPE) -> GraphQLRequest(query = request.awaitBody())
                    contentType.includes(APPLICATION_JSON) -> {
                        objectMapper.convertValue(request.awaitBody<Map<String, Any?>>())
                    }
                    contentType.includes(MULTIPART_FORM_DATA) -> {
                        val data = request.awaitMultipartData()

                        val operations = data.getFirst("operations")
                            ?.toByteArray()?.decodeToString()
                            ?.let { objectMapper.readValue<GraphQLServerRequest>(it) }
                            ?: return null

                        val variableBindings = data.getFirst("map")
                            ?.toByteArray()?.decodeToString()
                            ?.let { objectMapper.readValue<Map<String, List<String>>>(it) }
                            ?: return null

                        val newVariables = variableBindings
                            .toList()
                            .flatMap { (name, ref) ->
                                ref.map { it.split(".").last() to data.getFirst(name) }
                            }
                            .toMap()

                        when (operations) {
                            is GraphQLRequest -> {
                                operations.copy(
                                    variables = operations.variables?.plus(newVariables)
                                )
                            }
                            is GraphQLBatchRequest -> {
                                operations.copy(
                                    requests = operations.requests.map {
                                        it.copy(variables = it.variables?.plus(newVariables))
                                    }
                                )
                            }
                        }
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

    companion object {
        private val GRAPHQL_MEDIA_TYPE = MediaType("application", "graphql")
    }
}
