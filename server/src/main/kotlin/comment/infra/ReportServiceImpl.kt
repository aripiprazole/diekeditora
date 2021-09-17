package com.diekeditora.comment.infra

import com.diekeditora.comment.domain.Comment
import com.diekeditora.comment.domain.ReportReason
import com.diekeditora.comment.domain.ReportService
import com.diekeditora.id.domain.UniqueId
import graphql.relay.Connection
import org.springframework.stereotype.Service

@Service
class ReportServiceImpl : ReportService {
    override suspend fun findReportedComments(first: Int, after: UniqueId?): Connection<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun reportComment(comment: Comment, reason: ReportReason): Comment {
        TODO("Not yet implemented")
    }
}
