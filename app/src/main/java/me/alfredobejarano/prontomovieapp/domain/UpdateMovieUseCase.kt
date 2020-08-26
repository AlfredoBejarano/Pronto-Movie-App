package me.alfredobejarano.prontomovieapp.domain

import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.repository.MovieRepository
import javax.inject.Inject

/**
 * UpdateMovieUseCase
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class UpdateMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    suspend fun updateMovie(movie: Movie) = repository.updateMovie(movie)
}