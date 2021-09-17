package com.diekeditora.manga.infra

import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.profile.Profile
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface MangaProfileRepo : CursorBasedPaginationRepository<Profile, UUID> {
    @Query(
        """
        select p.* from profile p
        right join "user" u on u.id = p.owner_id
        left join manga_author ma on u.id = ma.user_id
        where ma.manga_id = :id
        """
    )
    suspend fun findAuthorsByManga(manga: Manga): List<Profile>
}
