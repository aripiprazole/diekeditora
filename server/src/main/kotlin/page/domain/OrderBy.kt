package com.diekeditora.page.domain

import org.springframework.data.domain.Sort

@Target(AnnotationTarget.PROPERTY)
annotation class OrderBy(val direction: Sort.Direction = Sort.Direction.ASC)
