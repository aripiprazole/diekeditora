package com.diekeditora.comment.infra

import com.diekeditora.domain.comment.Comment
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import java.util.UUID

internal interface CommentRepo : CoroutineSortingRepository<Comment, UUID>
