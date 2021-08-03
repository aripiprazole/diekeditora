package com.diekeditora.domain.genre

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("genre")
data class Genre(
    @Id
    @GraphQLIgnore
    val id: UniqueId? = null,
    val title: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : MutableEntity<Genre> {
    @GraphQLIgnore
    override val cursor: String
        @JsonIgnore
        get() = title

    @GraphQLIgnore
    override fun update(with: Genre): Genre {
        return copy(
            title = with.title,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Genre

        if (title != other.title) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        return title.hashCode()
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Genre(" +
            "title='$title', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
