package com.diekeditora.domain.image

import com.diekeditora.domain.profile.Profile

sealed class FileKind(val private: Boolean = false) {
    abstract fun generateKey(): String
}

class AvatarKind(val profile: Profile) : FileKind() {
    override fun generateKey(): String {
        return "profile-avatars/${profile.uid}"
    }
}
