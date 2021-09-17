package com.diekeditora.comment.domain

import com.diekeditora.id.domain.UniqueId
import org.valiktor.functions.hasSize
import org.valiktor.validate

data class CommentInput(
    val mangaUid: UniqueId,
    val content: String,
) {
    @Suppress("Detekt.MagicNumber")
    fun toComment(uid: UniqueId, mangaId: UniqueId, ownerId: UniqueId): Comment =
        validate(Comment(uid = uid, content = content, mangaId = mangaId, ownerId = ownerId)) {
            validate(Comment::content).hasSize(max = 144)
        }
}
