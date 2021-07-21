package com.diekeditora.infra.repositories

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.role.Role
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.entities.RoleAuthority
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.UUID

@Repository
interface RoleAuthorityRepo : CoroutineCrudRepository<RoleAuthority, UUID> {
    @Query(
        """
        select * from authority a
        left join role_authority ra on a.id = ra.authority_id
        where ra.role_id = :id
        order by ra.created_at
        limit :first
        """
    )
    suspend fun findAllByRole(role: Role, first: Int): Flow<Authority>

    @Query(
        """
        select * from authority a
        left join role_authority ra on a.id = ra.authority_id
        where ra.role_id = :id
        order by ra.created_at
        limit :first
            offset (
                select row_number()
                over (order by ra.created_at)
                from role_authority ra
                left join role r
                on r.id = ra.role_id
                where r.name = :after
            )
        """
    )
    suspend fun findAllByRole(role: Role, first: Int, after: String): Flow<Authority>

    @Query("""insert into role_authority(role_id, authority_id) values (:id, :authority)""")
    suspend fun link(role: Role, authority: UniqueId)

    @Query("""select count(*) from role_authority""")
    suspend fun totalEntries(): Long

    @Query(
        """
        delete from role_authority
        where role_id = :id
        and authority_id in (select id from authority where value in (:authorities))
        """
    )
    suspend fun unlink(role: Role, authorities: Iterable<String>)

    @Query(
        """
        select row_number() over (order by ra.created_at)
        from role_authority ra
        left join authority a on ra.authority_id = a.id
        where a.value = :authority
        """
    )
    suspend fun index(authority: String): BigInteger
}
