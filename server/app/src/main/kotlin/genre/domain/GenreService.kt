package genre.domain

interface GenreService {
    suspend fun findGenres(): List<Genre>

    suspend fun createGenre(title: String): Genre

    suspend fun deleteGenre(title: String)
}
