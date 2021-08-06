package com.diekeditora.infra.manga

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.id.UniqueIdService
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.manga.MangaInput
import com.diekeditora.domain.manga.MangaService
import com.diekeditora.domain.manga.MangaSort
import com.diekeditora.domain.profile.Profile
import com.diekeditora.infra.repo.findAllAsConnection
import graphql.relay.Connection
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order.asc
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
internal class MangaServiceImpl(
    val uidService: UniqueIdService,
    val repo: MangaRepo,
    val mangaProfileRepo: MangaProfileRepo,
) : MangaService {
    @Transactional
    override suspend fun findMangas(
        first: Int,
        after: UniqueId?,
        orderBy: MangaSort,
        filterBy: Set<String>
    ): Connection<Manga> {
        val order = when (orderBy) {
            MangaSort.Empty -> null
            MangaSort.Recent -> Sort.by(asc("created_at"))
            MangaSort.Older -> Sort.by(desc("created_at"))
            MangaSort.MostRead -> TODO()
            MangaSort.BestRated -> TODO()
        }

        return repo.findAllAsConnection(first, after?.toString(), order)
    }

    @Transactional
    override suspend fun findMangaByUid(uid: UniqueId): Manga? {
        return repo.findMangaByUid(uid)
    }

    @Transactional
    override suspend fun findAuthorsByManga(manga: Manga): List<Profile> {
        return mangaProfileRepo.findAuthorsByManga(manga).toList()
    }

    @Transactional
    override suspend fun saveManga(input: MangaInput): Manga {
        val manga = Manga(
            uid = uidService.generateUniqueId(),
            title = input.title,
            competing = input.competing,
            summary = input.summary,
            advisory = input.advisory,
        )

        return repo.save(manga)
    }

    @Transactional
    override suspend fun updateManga(manga: Manga, input: MangaInput): Manga {
        requireNotNull(manga.id) { "Manga id must be not null when updating" }

        val newManga = manga.update(
            Manga(
                uid = uidService.generateUniqueId(),
                title = input.title,
                competing = input.competing,
                summary = input.summary,
                advisory = input.advisory,
            ),
        )

        return repo.save(newManga)
    }

    @Transactional
    override suspend fun deleteManga(manga: Manga): Manga {
        return repo.save(manga.copy(deletedAt = LocalDateTime.now()))
    }
}