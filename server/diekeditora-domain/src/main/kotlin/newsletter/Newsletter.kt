package com.diekeditora.domain.newsletter

import com.diekeditora.domain.MutableEntity
import org.springframework.data.relational.core.mapping.Table

@Table("newsletter")
data class Newsletter(val email: String, val active: Boolean = true) : MutableEntity<Newsletter> {
    override fun update(with: Newsletter): Newsletter {
        return copy(email = with.email, active = with.active)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Newsletter

        if (email != other.email) return false
        if (active != other.active) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + active.hashCode()
        return result
    }

    override fun toString(): String {
        return "Newsletter(" +
            "email='$email', " +
            "active=$active" +
            ")"
    }
}
