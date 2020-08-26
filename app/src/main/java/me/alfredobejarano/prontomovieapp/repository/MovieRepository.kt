package me.alfredobejarano.prontomovieapp.repository

import me.alfredobejarano.prontomovieapp.datasource.local.CacheManager
import me.alfredobejarano.prontomovieapp.datasource.local.CacheManager.Companion.NO_MAX_PAGES
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
    private var page = cacheDataSource.getCurrentPage()
    private var maxPages = cacheDataSource.getMaxPages()

    private suspend fun getRemoteMovies() = remoteDataSource.getMovies(page).let {
        maxPages = it.total_pages ?: NO_MAX_PAGES
        page = (it.page ?: 1) + 1
        cacheDataSource.updatePagination(maxPages, page)
        it.results?.map(mapper::map)?.also { movies -> cacheRemoteMovies(movies) } ?: emptyList()
    }


    private suspend fun getLocalMovies(includeAdultMovies: Boolean = false) =
        localDataSource.readAll(includeAdultMovies)

    private suspend fun cacheRemoteMovies(remoteResult: List<Movie>) {
        remoteResult.forEach { movie -> localDataSource.createOrUpdate(movie) }
        cacheDataSource.createMovieCache()
    }

    suspend fun getMovies(includeAdultMovies: Boolean = false, nextPage: Boolean = false) = when {
        nextPage -> getRemoteMovies()
        cacheDataSource.isMovieCacheValid() -> getLocalMovies(includeAdultMovies)
        else -> getRemoteMovies()
    }

    suspend fun getFavoriteMovies() = localDataSource.readFavorites()

    suspend fun updateMovie(movie: Movie) = localDataSource.createOrUpdate(movie)
}