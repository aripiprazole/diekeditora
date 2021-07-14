package com.diekeditora.infra.services

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.manga.Comment
import com.diekeditora.domain.manga.CommentService
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

    override suspend fun createComment(manga: Manga, content: String): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(comment: Comment): Manga {
        TODO("Not yet implemented")
    }
}
