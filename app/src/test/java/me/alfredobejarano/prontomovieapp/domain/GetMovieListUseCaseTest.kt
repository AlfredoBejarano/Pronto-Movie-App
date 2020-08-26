package me.alfredobejarano.prontomovieapp.domain

import me.alfredobejarano.prontomovieapp.launchTest
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.repository.MovieRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMovieListUseCaseTest {
    @Mock
    private lateinit var mockRepository: MovieRepository
    private lateinit var testCandidate: GetMovieListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testCandidate = GetMovieListUseCase(mockRepository)
    }

    @Test
    fun `get movie list with adults movies`() = launchTest {
        val expectedMovies = listOf(
            Movie(id = 2, isAdult = true, popularity = 3.0),
            Movie(id = 1, isAdult = false, popularity = 2.5),
            Movie(id = 3, isAdult = true, popularity = 2.0),
            Movie(id = 4, isAdult = false, popularity = 1.5)
        )

        Mockito.`when`(mockRepository.getMovies(includeAdultMovies = true))
            .thenReturn(expectedMovies.shuffled())

        val result = testCandidate.getMovieList(true)

        assert(result.size == expectedMovies.size)

        result.forEachIndexed { index, item ->
            assert(expectedMovies[index].id == item.id)
        }
    }

    @Test
    fun `get movie list without adults movies`() = launchTest {
        val mockMovies = listOf(
            Movie(id = 2, isAdult = true, popularity = 3.0),
            Movie(id = 1, isAdult = false, popularity = 2.5),
            Movie(id = 3, isAdult = true, popularity = 2.0),
            Movie(id = 4, isAdult = false, popularity = 1.5)
        )

        val expectedMovies = listOf(
            Movie(id = 1, isAdult = false, popularity = 2.5),
            Movie(id = 4, isAdult = false, popularity = 1.5)
        )

        Mockito.`when`(mockRepository.getMovies(includeAdultMovies = true))
            .thenReturn(mockMovies.shuffled())

        val result = testCandidate.getMovieList(true)

        assert(result.size == expectedMovies.size)

        result.forEachIndexed { index, item ->
            assert(expectedMovies[index].id == item.id)
        }
    }
}