package com.diekeditora.app.resources.comment

import com.diekeditora.domain.comment.Comment
import com.diekeditora.domain.comment.CommentService
import com.diekeditora.domain.comment.ReportService
import com.diekeditora.domain.graphql.Secured
import com.diekeditora.domain.id.UniqueId
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import graphql.relay.Connection
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class CommentQuery(val reportService: ReportService, val commentService: CommentService) : Query {
    @Secured
    @PreAuthorize("hasAuthority('comment.view')")
    @GraphQLDescription("Returns reported comment page")
    suspend fun reportedComments(first: Int, after: UniqueId? = null): Connection<Comment> {
        return reportService.findReportedComments(first, after)
    }

    @Secured
    @PreAuthorize("hasAuthority('comment.view')")
    @GraphQLDescription("Returns comment detail by its uid")
    suspend fun comment(uid: UniqueId): Comment? {
        return commentService.findCommentByUid(uid)
    }
}
