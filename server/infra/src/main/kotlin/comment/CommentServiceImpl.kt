package com.diekeditora.infra.comment

import com.diekeditora.domain.comment.Comment
import com.diekeditora.domain.comment.CommentService
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
internal class CommentServiceImpl : CommentService {
    override suspend fun findComments(
        manga: Manga,
        first: Int,
        after: UniqueId?
    ): Connection<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun findCommentByUid(uid: UniqueId): Comment? {
        TODO("Not yet implemented")
    }

    override suspend fun createComment(manga: Manga, comment: Comment): Comment {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(comment: Comment): Comment {
        TODO("Not yet implemented")
    }
}
