package com.diekeditora.infra.repositories

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.role.Role
import com.diekeditora.domain.user.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRoleRepo {
    @Query(
        """
        select * from role r
        left join user_role ur on r.id = ur.role_id
        where ur.user_id = :id
        order by ur.created_at
        limit :first
        """
    )
    suspend fun findAllByUser(user: User, first: Int): Flow<Role>

    @Query(
        """
        select *
        from role r
        left join user_role ur on r.id = ur.role_id
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
    suspend fun findAllByUser(user: User, first: Int, after: String): Flow<Role>

    @Query(""" delete from user_role where user_id = :id and role_id in (:authorities)""")
    suspend fun unlink(user: User, authorities: Iterable<UniqueId>)

    @Query("""insert into user_authority(user_id, authority_id) values (:id, :authority)""")
    suspend fun link(user: User, authority: UniqueId)

    @Query("""select count(*) from role_authority""")
    suspend fun estimateTotalEntries(): Long

    @Query(
        """
        select row_number() over (order by ur.created_at)
        from user_role ur
        left join role r on ur.role_id = r.id
        where r.name = :name
        """
    )
    suspend fun findIndex(name: String): Long
}
