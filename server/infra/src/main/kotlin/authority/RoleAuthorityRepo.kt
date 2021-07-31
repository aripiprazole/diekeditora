package com.diekeditora.infra.authority

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.PaginationQuery
import com.diekeditora.domain.role.Role
import com.diekeditora.infra.repo.CursorBasedPaginationRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal interface RoleAuthorityRepo : CursorBasedPaginationRepository<Authority, UUID> {
    @PaginationQuery(
        selectQuery = """
            select a.* from authority a
            left join role_authority ra
            on a.id = ra.authority_id where ra.role_id = :owner
            order by ra.created_at limit :first
        """,
        offsetQuery = """
            select row_number()
            over (order by ra.created_at)
            from role_authority ra
            left join role r on r.id = ra.role_id
            where r.name = :after
        """
    )
    override fun findAll(first: Int, after: String?, sort: Sort?, owner: Any?): Flow<Authority>

    @Query(
        """
        select row_number() over (order by ra.created_at)
        from role_authority ra left join authority a on ra.authority_id = a.id
        where a.value = :authority
       """
    )
    override suspend fun indexOf(entity: Authority?): Long?

    @Query("""insert into role_authority(role_id, authority_id) values (:id, :authority)""")
    suspend fun link(role: Role, authority: UniqueId)

    @Query(
        """
        delete from role_authority
        where role_id = :id
        and authority_id in (select id from authority where value in (:authorities))
        """
    )
    suspend fun unlink(role: Role, authorities: Iterable<String>)
}
