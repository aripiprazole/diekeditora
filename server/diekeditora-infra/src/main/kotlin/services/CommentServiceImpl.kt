package com.diekeditora.infra.services

import com.diekeditora.domain.manga.Comment
import com.diekeditora.domain.manga.CommentService
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.page.Page
import org.springframework.stereotype.Service

@Service
internal class CommentServiceImpl : CommentService {
    override suspend fun findComments(manga: Manga, page: Int): Page<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun createComment(manga: Manga, content: String): Manga {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(comment: Comment): Manga {
        TODO("Not yet implemented")
    }
}
