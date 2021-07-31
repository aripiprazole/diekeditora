package com.diekeditora.infra.authority

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.PaginationQuery
import com.diekeditora.domain.user.User
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface UserAuthorityRepo : CursorBasedPaginationRepository<Authority, UUID> {
    @PaginationQuery(
        selectQuery = """
            select a.* from authority a
            left join user_authority ua
            on a.id = ua.authority_id where ua.user_id = :owner
            order by ua.created_at limit :first
        """,
        offsetQuery = """
            select row_number()
            over (order by ua.created_at)
            from user_authority ua
            left join role r
            on r.id = ua.user_id
            where r.name = :after
        """
    )
    override fun findAll(first: Int, after: String?, sort: Sort?, owner: Any?): Flow<Authority>

    @Query(
        """
        select row_number() over (order by ua.created_at)
        from user_authority ua
        left join authority a on ua.authority_id = a.id
        where a.value = :authority
        """
    )
    override suspend fun indexOf(entity: Authority?): Long?

    @Query("""insert into user_authority(user_id, authority_id) values (:id, :authority)""")
    suspend fun link(user: User, authority: UniqueId)

    @Query(
        """
        delete from user_authority
        where user_id = :id
        and authority_id in (select id from authority where value in (:authorities))
        """
    )
    suspend fun unlink(user: User, authorities: Iterable<String>)

    @Query(
        """
        select * from authority r
        left join user_authority ur on r.id = ur.authority_id
        where ur.user_id = :id
        order by ur.created_at
        """
    )
    fun findAllByUser(user: User): Flow<Authority>
}
