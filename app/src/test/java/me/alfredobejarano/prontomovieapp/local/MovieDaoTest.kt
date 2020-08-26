package me.alfredobejarano.prontomovieapp.local

import android.os.Build
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.prontomovieapp.datasource.local.CacheDataBase
import me.alfredobejarano.prontomovieapp.datasource.local.MovieDao
import me.alfredobejarano.prontomovieapp.model.local.Movie
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.random.Random

/**
 * MovieDaoTest
 */
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.LOLLIPOP])
@RunWith(RobolectricTestRunner::class)
class MovieDaoTest {
    private val scope = GlobalScope
    private lateinit var testCandidate: MovieDao

    @Before
    fun setup() {
        testCandidate = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.systemContext,
            CacheDataBase::class.java
        ).build().provideMovieDao()
    }

    @Test
    fun createTest() {
        scope.launch(Dispatchers.IO) {
            val testId = Random.nextInt()
            val testItem = Movie(id = testId)

            assert(testCandidate.readAll().isEmpty())

            testCandidate.createOrUpdate(testItem)

            assert(testCandidate.read(testId) == testItem)
        }
    }

    @Test
    fun readTest() {
        scope.launch(Dispatchers.IO) {
            assert(testCandidate.readAll().isEmpty())
            testCandidate.createOrUpdate(Movie(id = 1))
            assert(testCandidate.readAll().size == 1)
        }
    }

    @Test
    fun readAllTest() {
        scope.launch(Dispatchers.IO) {
            assert(testCandidate.readAll().isEmpty())
            for (i in 0 until 2) {
                testCandidate.createOrUpdate(Movie(id = i))
            }
            assert(testCandidate.readAll().size == 2)
        }
    }

    @Test
    fun readAllAdultsTest() {
        scope.launch(Dispatchers.IO) {
            assert(testCandidate.readAll().isEmpty())
            for (i in 0 until 10) {
                testCandidate.createOrUpdate(Movie(id = i, isAdult = (i % 5 == 0)))
            }
            assert(testCandidate.readAll(includeAdultMovies = true).size == 10)
            assert(testCandidate.readAll(includeAdultMovies = false).size == 5)
        }
    }

    @Test
    fun readFavorites() {
        scope.launch(Dispatchers.IO) {
            assert(testCandidate.readAll().isEmpty())
            for (i in 0 until 4) {
                testCandidate.createOrUpdate(Movie(id = i, isFavorite = i == 2))
            }
            assert(testCandidate.readFavorites().size == 1)
        }
    }

    @Test
    fun updateTest() {
        scope.launch(Dispatchers.IO) {
            val testItem = Movie(id = 1, isFavorite = false)

            testCandidate.createOrUpdate(testItem)
            assert(testCandidate.readFavorites().isEmpty())

            testItem.isFavorite = true
            testCandidate.createOrUpdate(testItem)

            assert(testCandidate.readFavorites().size == 1)
        }
    }
}