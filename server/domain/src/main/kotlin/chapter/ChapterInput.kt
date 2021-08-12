package com.diekeditora.domain.chapter

import com.diekeditora.domain.graphql.Max
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank
import com.diekeditora.domain.id.UniqueId

@Suppress("Detekt.MagicNumber")
data class ChapterInput(
    @NotBlank @Max(32) @Min(4) val title: String,
    @Min(0) val number: Int,
    val pages: Int,
    val enabled: Boolean = false,
) {
    fun toChapter(uid: UniqueId): Chapter {
        return Chapter(uid = uid, title = title, number = number, pages = pages, enabled = enabled)
    }

    companion object {
        fun from(chapter: Chapter): ChapterInput {
            return ChapterInput(
                title = chapter.title,
                number = chapter.number,
                pages = chapter.pages,
                enabled = chapter.enabled,
            )
        }
    }
}
