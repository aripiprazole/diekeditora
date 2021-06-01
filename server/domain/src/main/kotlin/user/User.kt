package com.diekeditora.domain.user

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Serializable(with = UserSerializer::class)
@Table("\"user\"")
@SerialName("User")
data class User(
    @Id
    @GraphQLIgnore
    val id: UUID? = null,
    val name: String,
    val email: String,
    val username: String,
    @GraphQLIgnore
    val password: String? = null,
    val birthday: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
    val emailVerifiedAt: LocalDateTime? = null,
) {
    @GraphQLIgnore
    fun update(user: User): User {
        return copy(
            username = user.username,
            name = user.name,
            email = user.email,
            password = user.password,
            updatedAt = LocalDateTime.now()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (birthday != other.birthday) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        result = 31 * result + (deletedAt?.hashCode() ?: 0)
        result = 31 * result + (emailVerifiedAt?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(" +
            "name='$name', " +
            "email='$email', " +
            "username='$username', " +
            "birthday=$birthday, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            "deletedAt=$deletedAt, " +
            "emailVerifiedAt=$emailVerifiedAt" +
            ")"
    }
}