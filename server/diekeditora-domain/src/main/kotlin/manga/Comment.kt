package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.profile.Profile
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.await
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("comment")
data class Comment(
    val uid: UniqueId,
    val profile: Profile,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) {
    @GraphQLDescription("Returns comment's likes")
    suspend fun likes(env: DataFetchingEnvironment): Int {
        return env
            .getDataLoader<Comment, Int>("CommentLikeLoader")
            .load(this)
            .await()
    }
}
