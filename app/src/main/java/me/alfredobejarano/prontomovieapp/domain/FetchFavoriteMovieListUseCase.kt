package me.alfredobejarano.prontomovieapp.domain

import me.alfredobejarano.prontomovieapp.model.local.Movie
import javax.inject.Inject

/**
 * GetMovieListUseCase
 */
class FetchFavoriteMovieListUseCase @Inject constructor() {
    suspend fun fetchFavorites(movies: List<Movie>) = movies.filter { it.isFavorite }

}