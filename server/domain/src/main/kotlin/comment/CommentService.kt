package com.diekeditora.domain.comment

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Manga
import graphql.relay.Connection

interface CommentService {
    suspend fun findComments(manga: Manga, first: Int, after: UniqueId? = null): Connection<Comment>

    suspend fun createComment(manga: Manga, content: String): Manga

    suspend fun deleteComment(comment: Comment): Comment
}
