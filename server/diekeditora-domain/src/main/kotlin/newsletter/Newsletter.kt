package com.diekeditora.domain.newsletter

import org.springframework.data.relational.core.mapping.Table

@Table("newsletter")
data class Newsletter(val email: String, val active: Boolean = true)
