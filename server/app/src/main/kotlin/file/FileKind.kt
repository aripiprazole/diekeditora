package com.diekeditora.app.file

import com.diekeditora.domain.chapter.Chapter
import com.diekeditora.domain.profile.Profile

sealed class FileKind(val private: Boolean = false) {
    abstract fun generateKey(): String
}

class ChapterCoverKind(val chapter: Chapter) : FileKind() {
    override fun generateKey(): String = "chapters/${chapter.uid}/cover"
}

class ChapterPageKind(val chapter: Chapter, val page: Int) : FileKind() {
    override fun generateKey(): String = "chapters/${chapter.uid}/pages/$page"
}

class AvatarKind(val profile: Profile) : FileKind() {
    override fun generateKey(): String = "profiles/avatar-${profile.uid}"
}
