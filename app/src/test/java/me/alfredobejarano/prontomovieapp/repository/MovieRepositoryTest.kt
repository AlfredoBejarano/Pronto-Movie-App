package me.alfredobejarano.prontomovieapp.repository

import me.alfredobejarano.prontomovieapp.datasource.local.CacheManager
import me.alfredobejarano.prontomovieapp.datasource.local.MovieDao
import me.alfredobejarano.prontomovieapp.datasource.remote.TheMoviesDbApiService
import me.alfredobejarano.prontomovieapp.launchTest
import me.alfredobejarano.prontomovieapp.model.MovieListResultObjectToMovieMapper
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.model.remote.MovieListResult
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

// Mockito marks TheMovieDbApiService.getMovies function as a stub, this runner will silence that warning.
@RunWith(MockitoJUnitRunner.Silent::class)
class MovieRepositoryTest {
    @Mock
    private lateinit var localDataSource: MovieDao

    @Mock
    private lateinit var cacheDataSource: CacheManager

    @Mock
    private lateinit var remoteDataSource: TheMoviesDbApiService

    @Mock
    private lateinit var mapper: MovieListResultObjectToMovieMapper

    private lateinit var testCandidate: MovieRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        testCandidate = MovieRepository(localDataSource, cacheDataSource, remoteDataSource, mapper)
    }

    @Test
    fun `get movies when cache is valid`() = launchTest {
        val mockList = emptyList<Movie>()
        Mockito.`when`(cacheDataSource.isMovieCacheValid()).thenReturn(true)

        val result = testCandidate.getMovies()
        assert(result == mockList)

        Mockito.verify(localDataSource, Mockito.times(1))
        Mockito.verify(cacheDataSource, Mockito.times(1))
        Mockito.verify(remoteDataSource, Mockito.never())
    }

    @Test
    fun `get movies when next page requested`() = launchTest {
        val mockList = MovieListResult()

        Mockito.`when`(remoteDataSource.getMovies()).thenReturn(mockList)

        val result = testCandidate.getMovies(nextPage = true)
        assert(result.size == mockList.results?.size)

        Mockito.verify(localDataSource, Mockito.never())
        Mockito.verify(cacheDataSource, Mockito.never())
        Mockito.verify(remoteDataSource, Mockito.times(1))
    }

    @Test
    fun `get movies when cache is invalid`() = launchTest {
        val mockList = MovieListResult()

        Mockito.`when`(remoteDataSource.getMovies()).thenReturn(mockList)
        Mockito.`when`(cacheDataSource.isMovieCacheValid()).thenReturn(false)

        val result = testCandidate.getMovies()
        assert(result.size == mockList.results?.size)

        Mockito.verify(localDataSource, Mockito.never())
        Mockito.verify(cacheDataSource, Mockito.times(2))
        Mockito.verify(remoteDataSource, Mockito.times(1))
    }
}