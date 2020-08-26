package me.alfredobejarano.prontomovieapp.repository

import me.alfredobejarano.prontomovieapp.datasource.local.CacheManager
import me.alfredobejarano.prontomovieapp.datasource.local.MovieDao
import me.alfredobejarano.prontomovieapp.datasource.remote.TheMoviesDbApiService
import me.alfredobejarano.prontomovieapp.model.MovieListResultObjectToMovieMapper
import me.alfredobejarano.prontomovieapp.model.local.Movie
import javax.inject.Inject

/**
 * MovieRepository
 */
class MovieRepository @Inject constructor(
    private val localDataSource: MovieDao,
    private val cacheDataSource: CacheManager,
    private val remoteDataSource: TheMoviesDbApiService,
    private val mapper: MovieListResultObjectToMovieMapper
) {
    private suspend fun getRemoteMovies() =
        remoteDataSource.getMovies().results?.map(mapper::map) ?: emptyList()

    private suspend fun getLocalMovies(includeAdultMovies: Boolean = false) =
        localDataSource.readAll(includeAdultMovies)

    private suspend fun cacheRemoteMovies(remoteResult: List<Movie>) {
        remoteResult.forEach { movie -> localDataSource.createOrUpdate(movie) }
        cacheDataSource.createMovieCache()
    }

    suspend fun getMovies(includeAdultMovies: Boolean = false) =
        if (cacheDataSource.isMovieCacheValid()) {
            getLocalMovies(includeAdultMovies)
        } else {
            getRemoteMovies().also { remoteResult -> cacheRemoteMovies(remoteResult) }
        }
}