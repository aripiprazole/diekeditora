package com.diekeditora.domain.chapter

import com.diekeditora.domain.id.UniqueId
import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class ChapterInput(
    val title: String,
    val index: Int,
    val releasedOn: LocalDate? = null,
    val enabled: Boolean = false,
) {
    fun toChapter(uid: UniqueId): Chapter {
        return Chapter(
            uid = uid,
            title = title,
            index = index,
            enabled = enabled,
            releasedOn = releasedOn,
        )
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
