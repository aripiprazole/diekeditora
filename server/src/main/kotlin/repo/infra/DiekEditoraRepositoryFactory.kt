package com.diekeditora.repo.infra

import org.springframework.data.r2dbc.convert.R2dbcConverter
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation
import org.springframework.data.repository.core.RepositoryInformation
import org.springframework.data.repository.core.RepositoryMetadata
import org.springframework.r2dbc.core.DatabaseClient

class DiekEditoraRepositoryFactory : R2dbcRepositoryFactory {
    private val repositoryInterface: Class<*>
    private val converter: R2dbcConverter
    private val operations: R2dbcEntityOperations

    constructor(
        repositoryInterface: Class<*>,
        operations: R2dbcEntityOperations
    ) : super(operations) {
        this.repositoryInterface = repositoryInterface
        this.operations = operations
        this.converter = operations.converter
    }

    constructor(
        repositoryInterface: Class<*>,
        client: DatabaseClient,
        dataAccessStrategy: ReactiveDataAccessStrategy,
    ) : super(client, dataAccessStrategy) {
        this.repositoryInterface = repositoryInterface
        this.operations = this::class.java.getDeclaredField("converter").get(this) as R2dbcEntityOperations
        this.converter = operations.converter
    }

    override fun getRepositoryBaseClass(metadata: RepositoryMetadata): Class<*> {
        return ReactiveCursorBasedPaginationRepositoryImpl::class.java
    }

    override fun getTargetRepository(information: RepositoryInformation): Any {
        @Suppress("UNCHECKED_CAST") val entityInformation =
            MappingRelationalEntityInformation<Any?, Any?>(
                converter.mappingContext.getRequiredPersistentEntity(information.domainType)
                        as RelationalPersistentEntity<Any?>
            )

        return getTargetRepositoryViaReflection(
            information, entityInformation,
            operations, converter, repositoryInterface
        )
    }
}
