package me.alfredobejarano.prontomovieapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import me.alfredobejarano.prontomovieapp.domain.FetchFavoriteMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.GetMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.UpdateMovieUseCase
import me.alfredobejarano.prontomovieapp.launchTest
import me.alfredobejarano.prontomovieapp.model.local.Movie
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieListViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockUpdateUseCase: UpdateMovieUseCase

    @Mock
    private lateinit var mockGetMovieListUseCase: GetMovieListUseCase

    @Mock
    private lateinit var mockFetchFavoriteUseCase: FetchFavoriteMovieListUseCase

    @Mock
    private lateinit var saveFavoriteMockObserver: Observer<in Unit?>

    @Mock
    private lateinit var movieListMockObserver: Observer<List<Movie>>

    private lateinit var testCandidate: MovieListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        testCandidate =
            MovieListViewModel(mockUpdateUseCase, mockGetMovieListUseCase, mockFetchFavoriteUseCase)
        testCandidate.movieListLiveData.observeForever(movieListMockObserver)
    }

    @Test
    fun getMovieList() = launchTest {
        Mockito.`when`(mockGetMovieListUseCase.getMovieList()).thenReturn(emptyList())
        testCandidate.getMovieList()
        Mockito.verify(movieListMockObserver, Mockito.times(1))
        Mockito.verify(movieListMockObserver).onChanged(emptyList())
    }

    @Test
    fun getMovieListNextPage() = launchTest {
        val mockList = listOf(Movie())
        Mockito.`when`(mockGetMovieListUseCase.getMovieList()).thenReturn(emptyList())
        testCandidate.getMovieList()
        Mockito.verify(movieListMockObserver, Mockito.times(1))
        assert(testCandidate.movieListLiveData.value?.size == mockList.size)
    }

    @Test
    fun reportFavoriteMovie() = launchTest {
        val mockMovie = Movie()
        val mockResult = Unit

        Mockito.`when`(mockUpdateUseCase.updateMovie(mockMovie)).thenReturn(mockResult)
        testCandidate.reportFavoriteMovie(mockMovie).observeForever(saveFavoriteMockObserver)

        Mockito.verify(saveFavoriteMockObserver, Mockito.times(1))
        Mockito.verify(saveFavoriteMockObserver).onChanged(mockResult)
    }

    @Test
    fun getFavorites() = launchTest {
        val mockFavorites = listOf(Movie(isFavorite = true))

        Mockito.`when`(mockFetchFavoriteUseCase.fetchFavorites()).thenReturn(mockFavorites)
        testCandidate.getFavorites()

        Mockito.verify(movieListMockObserver, Mockito.times(1))
        Mockito.verify(movieListMockObserver).onChanged(mockFavorites)
    }
}