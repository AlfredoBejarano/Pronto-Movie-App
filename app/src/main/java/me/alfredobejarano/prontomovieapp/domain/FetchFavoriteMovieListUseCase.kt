package me.alfredobejarano.prontomovieapp.domain

import me.alfredobejarano.prontomovieapp.repository.MovieRepository
import javax.inject.Inject

/**
 * GetMovieListUseCase
 */
class FetchFavoriteMovieListUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun fetchFavorites() = repository.getFavoriteMovies()

}