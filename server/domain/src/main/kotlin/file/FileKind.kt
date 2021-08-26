package com.diekeditora.domain.file

import com.diekeditora.domain.chapter.Chapter
import com.diekeditora.domain.manga.Manga
import com.diekeditora.domain.profile.Profile

sealed class FileKind(val private: Boolean = false) {
    abstract fun generateKey(): String
}

class ChapterCoverKind(val manga: Manga, val chapter: Chapter) : FileKind() {
    override fun generateKey(): String = "mangas/${manga.uid}/chapters/${chapter.uid}/cover"
}

class ChapterPageKind(val manga: Manga, val chapter: Chapter, val page: Int) : FileKind() {
    override fun generateKey(): String = "mangas/${manga.uid}/chapters/${chapter.uid}/pages/$page"
}

class AvatarKind(val profile: Profile) : FileKind() {
    override fun generateKey(): String = "profiles/avatar-${profile.uid}"
}
