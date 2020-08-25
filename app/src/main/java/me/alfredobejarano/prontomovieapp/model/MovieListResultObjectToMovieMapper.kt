package me.alfredobejarano.prontomovieapp.model

import me.alfredobejarano.prontomovieapp.BuildConfig
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.model.remote.MovieListResultObject

/**
 * MovieListResultObjectToMovieMapper
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class MovieListResultObjectToMovieMapper {
    fun map(input: MovieListResultObject) = input.run {
        Movie(
            id = id ?: 0,
            title = title.orEmpty(),
            popularity = popularity ?: 0.0,
            poster = "${BuildConfig.POSTER_BASE_URL}${posterPath}"
        )
    }
}