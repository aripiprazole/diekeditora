package com.diekeditora.domain.manga

import com.diekeditora.domain.id.UniqueId
import graphql.relay.Connection

interface CommentService {
    suspend fun findComments(manga: Manga, first: Int, after: UniqueId? = null): Connection<Comment>

    suspend fun createComment(manga: Manga, content: String): Manga

    suspend fun deleteComment(comment: Comment): Manga
}
