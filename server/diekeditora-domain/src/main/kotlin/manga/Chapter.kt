package com.diekeditora.domain.manga

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("chapter")
data class Chapter(
    @Id
    @GraphQLIgnore
    val id: UniqueId? = null,
    val title: String,
    val number: Int,
    val pages: Int,
    val enabled: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : MutableEntity<Chapter> {
    @GraphQLIgnore
    override val cursor: String
        @JsonIgnore
        get() = number.toString()

    @GraphQLIgnore
    override fun update(with: Chapter): Chapter {
        return copy(
            title = with.title,
            pages = with.pages,
            enabled = with.enabled,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chapter

        if (title != other.title) return false
        if (pages != other.pages) return false
        if (enabled != other.enabled) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + pages
        result = 31 * result + enabled.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Chapter(" +
            "title='$title', " +
            "pages=$pages, " +
            "enabled=$enabled, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
