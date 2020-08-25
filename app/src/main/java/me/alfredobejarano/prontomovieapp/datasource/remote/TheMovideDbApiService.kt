package me.alfredobejarano.prontomovieapp.datasource.remote

import me.alfredobejarano.prontomovieapp.model.remote.MovieListResult
import retrofit2.http.GET

/**
 * TheMovideDbApiService
 */
interface TheMovideDbApiService {
    /**
     * Retrieves a list of the most popular movies.
     */
    @GET("movie/popular")
    suspend fun getMovies(): MovieListResult
}