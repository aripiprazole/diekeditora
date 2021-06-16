package com.diekeditora.infra.serializers

import com.diekeditora.domain.user.User
import com.diekeditora.infra.serializers.graphql.DefaultConnectionSerializer
import com.diekeditora.infra.serializers.graphql.DefaultEdgeSerializer
import com.diekeditora.infra.serializers.graphql.DefaultPageInfoSerializer
import com.diekeditora.infra.serializers.graphql.GraphQLRequestSerializer
import com.diekeditora.infra.serializers.graphql.GraphQLResponseSerializer
import com.expediagroup.graphql.server.types.GraphQLResponse
import graphql.relay.Connection
import graphql.relay.DefaultConnection
import graphql.relay.DefaultEdge
import graphql.relay.Edge
import graphql.relay.PageInfo
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val JavaSerializersModule = SerializersModule {
    contextual(LocalDateSerializer)
    contextual(LocalDateTimeSerializer)
    contextual(InstantSerializer)
    contextual(UUIDSerializer)
}

val AnySerializersModule = SerializersModule {
    polymorphic(Any::class) {
        subclass(User.serializer())
    }
}

val GraphQLSerializersModule = SerializersModule {
    contextual(DefaultConnection::class) { DefaultConnectionSerializer(it.first()) }
    contextual(DefaultEdge::class) { DefaultEdgeSerializer(it.first()) }
    contextual(GraphQLResponse::class) { GraphQLResponseSerializer(it.first()) }
    contextual(GraphQLRequestSerializer)

    polymorphic(PageInfo::class) {
        subclass(DefaultPageInfoSerializer)
    }

    polymorphic(Connection::class) {
        default { DefaultConnectionSerializer(PolymorphicSerializer(Any::class)) }

        subclass(DefaultConnectionSerializer(PolymorphicSerializer(Any::class)))
    }

    polymorphic(Edge::class) {
        default { DefaultEdgeSerializer(PolymorphicSerializer(Any::class)) }

        subclass(DefaultEdgeSerializer(PolymorphicSerializer(Any::class)))
    }
}
