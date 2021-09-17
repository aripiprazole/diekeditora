package com.diekeditora.comment.infra

import com.diekeditora.comment.domain.Comment
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import java.util.UUID

interface CommentRepo : CoroutineSortingRepository<Comment, UUID>
