package com.diekeditora.comment.infra

import com.diekeditora.comment.domain.Comment
import com.diekeditora.comment.domain.CommentService
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl : CommentService {
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
