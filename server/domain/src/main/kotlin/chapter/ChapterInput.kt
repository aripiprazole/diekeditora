package com.diekeditora.domain.chapter

import com.diekeditora.domain.graphql.Future
import com.diekeditora.domain.graphql.Min
import com.diekeditora.domain.graphql.NotBlank
import com.diekeditora.domain.graphql.Size
import com.diekeditora.domain.id.UniqueId
import java.time.LocalDate

@Suppress("Detekt.MagicNumber")
data class ChapterInput(
    @NotBlank @Size(min = 4, max = 32) val title: String,
    @Min(0) val index: Int,
    @Future val releasedOn: LocalDate? = null,
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
