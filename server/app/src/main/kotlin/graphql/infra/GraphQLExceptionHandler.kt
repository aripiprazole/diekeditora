package graphql.infra

import com.expediagroup.graphql.server.extensions.toGraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import graphql.execution.SimpleDataFetcherExceptionHandler
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage
import java.util.Locale

class GraphQLExceptionHandler(
    private val delegate: DataFetcherExceptionHandler = SimpleDataFetcherExceptionHandler(),
) : DataFetcherExceptionHandler {
    override fun onException(
        handlerParameters: DataFetcherExceptionHandlerParameters,
    ): DataFetcherExceptionHandlerResult = when (val exception = handlerParameters.exception) {
        is ConstraintViolationException -> {
            val errors = exception.constraintViolations
                .mapToMessage(locale = Locale.ENGLISH)
                .map { Exception("ValidationError(${it.property}): ${it.message}").toGraphQLError() }

            DataFetcherExceptionHandlerResult
                .newResult()
                .errors(errors)
                .build()
        }
        else -> delegate.onException(handlerParameters)
    }
}
