package com.lorenzoog.diekeditora.entities

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
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
    @Transient
    @GraphQLIgnore
    val id: @Contextual UUID? = null,
    val username: String,
    val name: String,
    val email: String,
    @Transient
    @GraphQLIgnore
    val password: String? = null,
    val birthday: @Contextual LocalDate,
    val createdAt: @Contextual LocalDateTime = LocalDateTime.now(),
    val updatedAt: @Contextual LocalDateTime? = null,
    val emailVerifiedAt: @Contextual LocalDateTime? = null,
    val deletedAt: @Contextual LocalDateTime? = null,
) : Entity {
    fun withUsername(username: String?): User = copy(username = username ?: this.username)
    fun withName(name: String?): User = copy(name = name ?: this.name)
    fun withEmail(email: String?): User = copy(email = email ?: this.email)
    fun withPassword(password: String?) = copy(password = password ?: this.password)

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
