package com.diekeditora.comment.infra

import com.diekeditora.comment.domain.Comment
import com.diekeditora.comment.domain.CommentService
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import com.diekeditora.redis.domain.expiresIn
import com.diekeditora.redis.infra.CacheProvider
import graphql.relay.Connection
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import kotlin.time.ExperimentalTime

@Primary
@Service
@OptIn(ExperimentalTime::class)
internal class CachedCommentService(
    val delegate: CommentService,
    val cacheProvider: CacheProvider,
) : CommentService by delegate {
    override suspend fun findComments(
        manga: Manga,
        first: Int,
        after: UniqueId?
    ): Connection<Comment> {
        return cacheProvider
            .repo<Connection<Comment>>()
            .remember("mangaCommentConnection.${manga.cursor}.$first.$after", expiresIn) {
                delegate.findComments(manga, first, after)
            }
    }

    override suspend fun deleteComment(comment: Comment): Comment {
        return delegate.deleteComment(comment).also {
            cacheProvider.repo<Comment>().delete("comment.${comment.cursor}")
        }
    }
}
