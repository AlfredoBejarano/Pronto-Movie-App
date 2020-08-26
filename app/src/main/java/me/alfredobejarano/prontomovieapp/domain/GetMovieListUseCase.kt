package me.alfredobejarano.prontomovieapp.domain

import me.alfredobejarano.prontomovieapp.repository.MovieRepository
import javax.inject.Inject

/**
 * GetMovieListUseCase
 */
class GetMovieListUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun getMovieList(includeAdultMovies: Boolean = false) =
        repository.getMovies(includeAdultMovies).apply {
            sortedWith(compareBy { it.popularity }).reversed()
        }
}