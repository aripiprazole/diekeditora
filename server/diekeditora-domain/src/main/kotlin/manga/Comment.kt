package com.diekeditora.domain.manga

import com.diekeditora.domain.MutableEntity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.fasterxml.jackson.annotation.JsonIgnore
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("comment")
data class Comment(
    @Id
    @GraphQLIgnore
    @JsonIgnore
    val id: UniqueId? = null,
    val uid: UniqueId,
    val profile: Profile,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) : MutableEntity<Comment> {
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
            profile = with.profile,
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
        if (profile != other.profile) return false
        if (content != other.content) return false

        return true
    }

    @GraphQLIgnore
    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + profile.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }

    @GraphQLIgnore
    override fun toString(): String {
        return "Comment(" +
            "uid=$uid, " +
            "profile=$profile, " +
            "content='$content', " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt" +
            ")"
    }
}
