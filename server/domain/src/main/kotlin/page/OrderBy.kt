package com.diekeditora.domain.page

import org.springframework.data.domain.Sort

annotation class OrderBy(val property: String, val direction: Sort.Direction = Sort.Direction.ASC)
