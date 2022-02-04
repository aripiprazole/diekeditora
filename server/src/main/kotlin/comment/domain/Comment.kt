package com.diekeditora.comment.domain

import com.diekeditora.database.domain.CommentId
import com.diekeditora.database.domain.MangaId
import com.diekeditora.database.domain.UserId
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.page.domain.Cursor
import com.diekeditora.page.domain.OrderBy
import com.diekeditora.profile.domain.Profile
import com.diekeditora.shared.domain.BelongsTo
import com.diekeditora.shared.domain.MutableEntity
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations
import com.expediagroup.graphql.generator.annotations.GraphQLValidObjectLocations.Locations
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@GraphQLValidObjectLocations([Locations.OBJECT])
@Table("comment")
data class Comment(
    @GraphQLIgnore
    @Id override val id: CommentId = CommentId.New,
    @Cursor val uid: UniqueId,
    val content: String,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    @GraphQLIgnore val mangaId: MangaId,
    @GraphQLIgnore override val ownerId: UserId,
) : MutableEntity<Comment, CommentId>, BelongsTo<UserId> {
    @GraphQLDescription("Returns the comment's author")
    suspend fun profile(env: DataFetchingEnvironment): Profile {
        return env
            .getDataLoader<Comment, Profile>("CommentAuthorLoader")
            .load(this)
            .await()
    }

    @GraphQLDescription("Returns the comment's likes")
    suspend fun likes(env: DataFetchingEnvironment): Int {
        return env
            .getDataLoader<Comment, Int>("CommentLikeLoader")
            .load(this)
            .await()
    }

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
