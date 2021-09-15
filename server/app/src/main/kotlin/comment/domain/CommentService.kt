package com.diekeditora.app.comment.domain

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import graphql.relay.Connection

interface CommentService {
    suspend fun findComments(manga: Manga, first: Int, after: UniqueId? = null): Connection<Comment>

    suspend fun findCommentByUid(uid: UniqueId): Comment?

    suspend fun createComment(manga: Manga, comment: Comment): Comment

    suspend fun deleteComment(comment: Comment): Comment
}
