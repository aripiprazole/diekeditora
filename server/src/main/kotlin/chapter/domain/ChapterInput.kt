package com.diekeditora.chapter.domain

import com.diekeditora.database.domain.MangaId
import com.diekeditora.id.domain.UniqueId
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate
import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class ChapterInput(
    val mangaUid: UniqueId,
    val title: String,
    val index: Int,
    val releasedOn: LocalDate? = null,
    val enabled: Boolean = false,
) {
    fun toChapter(uid: UniqueId, mangaId: MangaId): Chapter = validate(
        Chapter(
            uid = uid, title = title, index = index,
            enabled = enabled, releasedOn = releasedOn, ownerId = mangaId
        )
    ) {
        validate(Chapter::title).hasSize(min = 4, max = 32)
        validate(Chapter::index).isGreaterThan(0)
        validate(Chapter::releasedOn).isGreaterThan(LocalDate.now())
    }
}
