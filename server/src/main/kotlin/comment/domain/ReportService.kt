package com.diekeditora.comment.domain

import com.diekeditora.id.domain.UniqueId
import graphql.relay.Connection

interface ReportService {
    suspend fun findReportedComments(first: Int, after: UniqueId? = null): Connection<Comment>

    suspend fun reportComment(comment: Comment, reason: ReportReason): Comment
}
