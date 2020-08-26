package me.alfredobejarano.prontomovieapp.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Movie
 *
 * UI driven model class that represents a movie title.
 */
@Entity(tableName = "Movies")
data class Movie(
    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val title: String = "",
    val poster: String = "",
    @ColumnInfo(name = "is_adult")
    val isAdult: Boolean = false,
    val popularity: Double = 0.0,
    val isFavorite: Boolean = false
) : Comparable<Movie> {
    override fun compareTo(other: Movie) = if (id == other.id) 0 else -1
}