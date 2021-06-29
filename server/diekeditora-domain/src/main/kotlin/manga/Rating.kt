package com.diekeditora.domain.manga

data class Rating(val value: Int) {
    init {
        require(value in MIN_RATING..MAX_RATING)
    }

    companion object {
        private const val MIN_RATING = 1
        private const val MAX_RATING = 5
    }
}
