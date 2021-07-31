@file:Suppress("DEPRECATION")

package com.diekeditora.infra.repo

import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.r2dbc.core.DatabaseClient

class DiekEditoraRepositoryFactoryBean<T : Repository<S, ID>, S, ID : java.io.Serializable>(
    repositoryInterface: Class<out T>,
) : R2dbcRepositoryFactoryBean<T, S, ID>(repositoryInterface) {
    override fun getFactoryInstance(operations: R2dbcEntityOperations): RepositoryFactorySupport {
        return DiekEditoraRepositoryFactory(operations)
    }

    override fun getFactoryInstance(
        client: DatabaseClient,
        dataAccessStrategy: ReactiveDataAccessStrategy
    ): RepositoryFactorySupport {
        return DiekEditoraRepositoryFactory(client, dataAccessStrategy)
    }
}

internal class DiekEditoraRepositoryFactory : R2dbcRepositoryFactory {
    constructor(operations: R2dbcEntityOperations) : super(operations)

    constructor(client: DatabaseClient, dataAccessStrategy: ReactiveDataAccessStrategy) : super(
        client,
        dataAccessStrategy
    )

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return ReactiveCursorBasedPaginationRepositoryImpl::class.java
    }
}
