package com.diekeditora.chapter.infra

import com.diekeditora.chapter.domain.Chapter
import com.diekeditora.chapter.domain.ChapterService
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.manga.domain.Manga
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
internal class ChapterServiceImpl : ChapterService {
    override suspend fun findChapterByUid(uid: UniqueId): Chapter? {
        TODO("Not yet implemented")
    }

    override suspend fun findChapters(
        manga: Manga,
        first: Int,
        after: UniqueId?
    ): Connection<Chapter> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChapter(target: Chapter, input: Chapter): Chapter {
        TODO("Not yet implemented")
    }

    override suspend fun createChapter(manga: Manga, chapter: Chapter): Chapter {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChapter(chapter: Chapter): Chapter {
        TODO("Not yet implemented")
    }
}
