package me.alfredobejarano.prontomovieapp.model

import me.alfredobejarano.prontomovieapp.model.remote.MovieListResultObject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieListResultObjectToMovieMapperTest {
    private val testCandidate = MovieListResultObjectToMovieMapper()

    @Test
    fun map() {
        val mockItems = mutableListOf<MovieListResultObject>()
        for (i in 0 until 25) {
            mockItems.add(MovieListResultObject(id = i))
        }
        val resultItems = mockItems.map(testCandidate::map)
        assert(resultItems.size == mockItems.size)

        for (i in 0 until 25) {
            assert(resultItems[i].id == mockItems[i].id)
        }
    }
}