@file:UseSerializers(
    UUIDSerializer::class,
    InstantSerializer::class,
    LocalDateTimeSerializer::class,
    LocalDateSerializer::class
)

package com.lorenzoog.diekeditora.entities

import com.lorenzoog.diekeditora.serializers.InstantSerializer
import com.lorenzoog.diekeditora.serializers.LocalDateSerializer
import com.lorenzoog.diekeditora.serializers.LocalDateTimeSerializer
import com.lorenzoog.diekeditora.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Table("users")
@Serializable
data class User(
    @Id
    @Transient
    val id: Long = 0,
    val uid: UUID = UUID.randomUUID(),
    val name: String,
    val username: String,
    val email: String,
    @Transient
    val password: String = "",
    val birthday: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val emailVerifiedAt: LocalDateTime? = null,
    val deletedAt: Instant? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (uid != other.uid) return false
        if (name != other.name) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (birthday != other.birthday) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false
        if (emailVerifiedAt != other.emailVerifiedAt) return false
        if (deletedAt != other.deletedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (emailVerifiedAt?.hashCode() ?: 0)
        result = 31 * result + (deletedAt?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(uid=$uid" +
            ", name='$name'" +
            ", username='$username'" +
            ", email='$email'" +
            ", birthday=$birthday" +
            ", createdAt=$createdAt" +
            ", updatedAt=$updatedAt" +
            ", emailVerifiedAt=$emailVerifiedAt" +
            ", deletedAt=$deletedAt)"
    }
}
