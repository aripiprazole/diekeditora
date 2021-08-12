package com.diekeditora.domain.comment

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.diekeditora.domain.profile.Profile
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("comment")
data class Comment(
    @GraphQLIgnore
    @Id val id: UniqueId? = null,
    @Cursor val uid: UniqueId,
    val content: String,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
    @GraphQLIgnore val authorId: UniqueId,
) : MutableEntity<Comment> {
    @GraphQLDescription("Returns comment's author")
    suspend fun profile(env: DataFetchingEnvironment): Profile {
        return env
            .getDataLoader<Comment, Profile>("CommentAuthorLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns comment's likes")
    suspend fun likes(env: DataFetchingEnvironment): Int {
        return env
            .getDataLoader<Comment, Int>("CommentLikeLoader")
            .load(this)
            .await()
    }

    @GraphQLIgnore
    override val cursor: String
        @JsonIgnore
        get() = uid.value

    @GraphQLIgnore
    override fun update(with: Comment): Comment {
        return copy(
            content = with.content,
            updatedAt = LocalDateTime.now(),
        )
    }

    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (uid != other.uid) return false
        if (content != other.content) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Comment(" +
            "uid=$uid, " +
            "content='$content', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
