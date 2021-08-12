package com.diekeditora.domain.chapter

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("chapter")
data class Chapter(
    @GraphQLIgnore
    @Id val id: UniqueId? = null,
    @Cursor val uid: UniqueId,
    val title: String,
    val index: Int,
    val enabled: Boolean = false,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val releasedOn: LocalDate? = null,
    val updatedAt: LocalDateTime? = null,
) : MutableEntity<Chapter> {
    @GraphQLIgnore
    override fun update(with: Chapter): Chapter {
        return copy(
            title = with.title,
            enabled = with.enabled,
            releasedOn = with.releasedOn,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chapter

        if (title != other.title) return false
        if (enabled != other.enabled) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + enabled.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Chapter(" +
            "title='$title', " +
            "enabled=$enabled, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
