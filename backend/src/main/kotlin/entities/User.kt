package com.lorenzoog.diekeditora.entities

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.lorenzoog.diekeditora.dtos.Secret
import com.lorenzoog.diekeditora.dtos.asSecret
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Table("\"user\"")
@Serializable
data class User(
    @Id
    @GraphQLIgnore
    @Transient
    val id: UUID? = null,
    val username: String,
    val name: String,
    val email: String,
    @GraphQLIgnore
    val password: Secret = Secret.None,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null,
    val emailVerifiedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
) : Entity {
    @GraphQLIgnore
    fun withUsername(username: String?): User = copy(username = username ?: this.username)

    @GraphQLIgnore
    fun withName(name: String?): User = copy(name = name ?: this.name)

    @GraphQLIgnore
    fun withEmail(email: String?): User = copy(email = email ?: this.email)

    @GraphQLIgnore
    fun withPassword(password: String?) = copy(password = password?.asSecret() ?: this.password)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

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
        var result = name.hashCode()
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
        return "User(" +
            "username='$username', " +
            "name='$name', " +
            "email='$email', " +
            "birthday=$birthday, " +
            "createdAt=$createdAt, " +
            "updatedAt=$updatedAt, " +
            "emailVerifiedAt=$emailVerifiedAt, " +
            "deletedAt=$deletedAt" +
            ")"
    }
}
