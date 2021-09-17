package com.diekeditora.chapter.domain

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import graphql.relay.Connection

interface ChapterService {
    suspend fun findChapterByUid(uid: UniqueId): Chapter?

    suspend fun findChapters(manga: Manga, first: Int, after: UniqueId? = null): Connection<Chapter>

    suspend fun updateChapter(target: Chapter, input: Chapter): Chapter

    suspend fun createChapter(manga: Manga, chapter: Chapter): Chapter

    suspend fun deleteChapter(chapter: Chapter): Chapter
}
