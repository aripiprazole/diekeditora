package com.diekeditora.genre.domain

import com.diekeditora.shared.domain.MutableEntity
import com.diekeditora.page.domain.Cursor
import com.diekeditora.shared.refs.GenreId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("genre")
data class Genre(
    @GraphQLIgnore
    @Id override val id: GenreId = GenreId.New,
    @Cursor val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : MutableEntity<Genre, GenreId> {
    @GraphQLIgnore
    override fun update(with: Genre): Genre {
        return copy(
            name = with.name,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Genre

        if (name != other.name) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        return name.hashCode()
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Genre(" +
            "title='$name', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
