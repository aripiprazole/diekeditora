package com.diekeditora.domain.chapter

import com.diekeditora.domain.id.UniqueId
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate
import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class ChapterInput(
    val title: String,
    val index: Int,
    val releasedOn: LocalDate? = null,
    val enabled: Boolean = false,
) {
    fun toChapter(uid: UniqueId): Chapter = validate(
        Chapter(uid = uid, title = title, index = index, enabled = enabled, releasedOn = releasedOn)
    ) {
        validate(Chapter::title).hasSize(min = 4, max = 32)
        validate(Chapter::index).isGreaterThan(0)
        validate(Chapter::releasedOn).isGreaterThan(LocalDate.now())
    }

    companion object {
        fun from(chapter: Chapter): ChapterInput {
            return ChapterInput(
                title = chapter.title,
                index = chapter.index,
                enabled = chapter.enabled,
            )
        }
    }
}
