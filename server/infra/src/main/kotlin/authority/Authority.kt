package com.diekeditora.infra.authority

import com.diekeditora.domain.Entity
import com.diekeditora.domain.id.UniqueId
import com.diekeditora.domain.page.Cursor
import com.diekeditora.domain.page.OrderBy
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("authority")
internal data class Authority(
    @GraphQLIgnore
    @Id override val id: UniqueId? = null,
    @Cursor val value: String,
    @OrderBy val createdAt: LocalDateTime = LocalDateTime.now(),
) : Entity<Authority> {
    @GraphQLIgnore
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Authority

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "Authority($value)"
    }

    companion object {
        fun of(value: String): Authority {
            return Authority(null, value)
        }
    }
}
