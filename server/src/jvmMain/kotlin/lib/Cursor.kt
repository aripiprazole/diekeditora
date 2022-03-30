package com.diekeditora.lib

import kotlinx.serialization.Serializable

@Serializable
class Cursor<T>(val items: List<T>)
