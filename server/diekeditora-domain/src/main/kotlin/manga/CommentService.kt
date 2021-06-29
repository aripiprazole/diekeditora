package com.diekeditora.domain.manga

import com.diekeditora.domain.page.Page

interface CommentService {
    suspend fun findComments(manga: Manga, page: Int = 1): Page<Comment>

    suspend fun createComment(manga: Manga, content: String): Manga

    suspend fun deleteComment(comment: Comment): Manga
}
