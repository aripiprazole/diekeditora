package com.diekeditora.shared

import com.expediagroup.graphql.server.execution.KotlinDataLoader
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import java.util.concurrent.Executors

private const val POOL_SIZE = 4

private val dispatcher = Executors.newFixedThreadPool(POOL_SIZE).asCoroutineDispatcher()
private val futureScope = CoroutineScope(CoroutineName("FutureScope") + dispatcher)

fun dataLoader(name: String): DataLoaderBuilder {
    return DataLoaderBuilder(name)
}

class DataLoaderBuilder internal constructor(private val name: String) {
    fun <K, V> execute(function: suspend (K) -> V): KotlinDataLoader<K, V> {
        return object : KotlinDataLoader<K, V> {
            override val dataLoaderName = name

            override fun getDataLoader(): DataLoader<K, V> = DataLoader { keys ->
                futureScope.future {
                    keys.map { function(it) }
                }
            }
        }
    }
}
