@file:Suppress("DEPRECATION")

package com.diekeditora.repo.infra

import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactoryBean
import org.springframework.data.repository.Repository
import org.springframework.data.repository.core.support.RepositoryFactorySupport
import org.springframework.r2dbc.core.DatabaseClient

class DiekEditoraRepositoryFactoryBean<T : Repository<S, ID>, S, ID : java.io.Serializable>(
    private val repositoryInterface: Class<out T>,
) : R2dbcRepositoryFactoryBean<T, S, ID>(repositoryInterface) {
    override fun getFactoryInstance(operations: R2dbcEntityOperations): RepositoryFactorySupport {
        return DiekEditoraRepositoryFactory(repositoryInterface, operations)
    }

    override fun getFactoryInstance(
        client: DatabaseClient,
        dataAccessStrategy: ReactiveDataAccessStrategy
    ): RepositoryFactorySupport {
        return DiekEditoraRepositoryFactory(repositoryInterface, client, dataAccessStrategy)
    }
}
