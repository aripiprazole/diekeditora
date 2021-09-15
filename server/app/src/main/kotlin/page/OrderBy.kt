package com.diekeditora.app.page

import org.springframework.data.domain.Sort

@Target(AnnotationTarget.PROPERTY)
annotation class OrderBy(val direction: Sort.Direction = Sort.Direction.ASC)
