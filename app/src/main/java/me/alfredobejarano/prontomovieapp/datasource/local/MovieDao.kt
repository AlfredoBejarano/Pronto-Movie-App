package me.alfredobejarano.prontomovieapp.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.alfredobejarano.prontomovieapp.model.local.Movie

/**
 * MovieDao
 */
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(movie: Movie)

    @Query("SELECT * FROM Movies WHERE isFavorite = 1 ORDER BY popularity DESC ")
    suspend fun readFavorites(): List<Movie>

    @Query("SELECT * FROM Movies WHERE pk = :movieId LIMIT 1")
    suspend fun read(movieId: Int): Movie?

    @Query("SELECT * FROM Movies WHERE is_adult = :includeAdultMovies ORDER BY popularity DESC ")
    suspend fun readAll(includeAdultMovies: Boolean = false): List<Movie>
}