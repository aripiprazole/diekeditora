package com.diekeditora.app.comment.domain

import com.diekeditora.domain.id.UniqueId
import graphql.relay.Connection

interface ReportService {
    suspend fun findReportedComments(first: Int, after: UniqueId? = null): Connection<Comment>

    suspend fun reportComment(comment: Comment, reason: ReportReason): Comment
}
