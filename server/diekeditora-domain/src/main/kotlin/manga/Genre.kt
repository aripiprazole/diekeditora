package com.diekeditora.domain.manga

import com.diekeditora.domain.Entity
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
    @JsonIgnore
    val id: UniqueId? = null,
    val title: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : Entity<Genre> {
    override fun update(with: Genre): Genre {
        return copy(
            title = with.title,
            updatedAt = LocalDateTime.now(),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Genre

        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    override fun toString(): String {
        return "Genre(" +
            "title='$title', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
