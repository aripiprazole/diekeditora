package com.diekeditora.shared.infra

import com.expediagroup.graphql.server.execution.KotlinDataLoader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderOptions

fun dataLoader(name: String): DataLoaderBuilder {
    return DataLoaderBuilder(name)
}

class DataLoaderBuilder internal constructor(private val name: String) {
    fun <K, V> execute(function: suspend (K) -> V): KotlinDataLoader<K, V> {
        return object : KotlinDataLoader<K, V> {
            override val dataLoaderName = name

            override fun getDataLoader(): DataLoader<K, V> = DataLoader.newDataLoader(
                { keys ->
                    GlobalScope.future { keys.map { function(it) } }
                },
                DataLoaderOptions.newOptions()
                    .setCachingEnabled(false)
                    .setBatchingEnabled(false)
            )
        }
    }
}
