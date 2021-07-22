package com.diekeditora.infra.repositories

import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.user.User
import com.diekeditora.infra.entities.Authority
import com.diekeditora.infra.entities.UserAuthority
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.UUID

@Repository
internal interface UserAuthorityRepo : CoroutineCrudRepository<UserAuthority, UUID> {
    @Query(
        """
        select * from authority r
        left join user_authority ur on r.id = ur.authority_id
        where ur.user_id = :id
        order by ur.created_at
        """
    )
    suspend fun findAllByUser(user: User): Flow<Authority>

    @Query(
        """
        select * from authority r
        left join user_authority ur on r.id = ur.authority_id
        where ur.user_id = :id
        order by ur.created_at
        limit :first
        """
    )
    suspend fun findAllByUser(user: User, first: Int): Flow<Authority>

    @Query(
        """
        select *
        from authority r
        left join user_authority ur on r.id = ur.authority_id
        order by ur.created_at
        limit :first
            offset (
                select row_number()
                over (order by ur.created_at)
                from user_authority ur
                left join authority r
                on r.id = ur.authority_id
                where r.value = :after
            )
        """
    )
    suspend fun findAllByUser(user: User, first: Int, after: String): Flow<Authority>

    @Query("""insert into user_authority(user_id, authority_id) values (:id, :authority)""")
    suspend fun link(user: User, authority: UniqueId)

    @Query("""select count(*) from user_authority""")
    suspend fun totalEntries(): Long

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
        select row_number() over (order by ua.created_at)
        from user_authority ua
        left join authority a on ua.authority_id = a.id
        where a.value = :authority
        """
    )
    suspend fun index(authority: String): BigInteger
}
