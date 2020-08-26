package me.alfredobejarano.prontomovieapp.datasource.remote

import me.alfredobejarano.prontomovieapp.model.remote.MovieListResult
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * TheMovideDbApiService
 */
interface TheMoviesDbApiService {
    /**
     * Retrieves a list of the most popular movies.
     */
    @GET("movie/popular")
    suspend fun getMovies(@Query("page") page: Int = 1): MovieListResult
}