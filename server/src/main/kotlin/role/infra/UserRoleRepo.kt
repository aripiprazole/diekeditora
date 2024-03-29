package com.diekeditora.role.infra

import com.diekeditora.id.domain.UniqueId
import com.diekeditora.page.domain.PaginationQuery
import com.diekeditora.repo.domain.CursorBasedPaginationRepository
import com.diekeditora.role.domain.Role
import com.diekeditora.user.domain.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRoleRepo : CursorBasedPaginationRepository<Role, UUID> {
    @PaginationQuery(
        selectQuery = """
            select * from role r
            left join user_role ur on r.id = ur.role_id
            where ur.user_id = :owner
            order by ur.created_at
            limit :first
        """,
        offsetQuery = """
            select *
            from role r
            left join user_role ur on r.id = ur.role_id
            where ur.user_id = :owner
            order by ur.created_at
            limit :first
                offset (
                    select row_number()
                    over (order by ur.created_at)
                    from user_role ur
                    left join role r
                    on r.id = ur.role_id
                    where r.name = :after
            )
        """
    )
    override fun findAll(first: Int, after: String?, sort: Sort?, owner: Any?): Flow<Role>

    @Query(
        """
        select row_number() over (order by ur.created_at)
        from user_role ur
        left join role r on ur.role_id = r.id
        where r.name = :name
        """
    )
    override suspend fun indexOf(entity: Role?): Long?

    @Query(""" delete from user_role where user_id = :id and role_id in (:authorities)""")
    suspend fun unlink(user: User, authorities: Iterable<UniqueId>)

    @Query("""insert into user_authority(user_id, authority_id) values (:id, :authority)""")
    suspend fun link(user: User, authority: UniqueId)

    @Query("""select u.* from "user" u left join user_role ur on u.id = ur.user_id where ur.role_id = :id""")
    suspend fun findUsersByRole(role: Role): Flow<User>
}
