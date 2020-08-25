package me.alfredobejarano.prontomovieapp.model.remote

import com.google.gson.annotations.SerializedName

/**
 * MovieListResultObject
 *
 * Defines the JSON schema from TheMovideDB API for a movie within a query result.
 */
data class MovieListResultObject(
    @SerializedName("poster_path")
    val posterPath: String? = "",
    @SerializedName("adult")
    val adult: Boolean? = true,
    @SerializedName("overview")
    val overview: String? = "",
    @SerializedName("release_date")
    val releaseDate: String? = "",
    @SerializedName("genre_ids")
    val genresIds: List<Int> = emptyList(),
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("original_title")
    val originalTitle: String? = "",
    @SerializedName("")
    val originalLanguage: String? = "original_language",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    @SerializedName("popularity")
    val popularity: Double? = 0.0,
    @SerializedName("vote_count")
    val voteCount: Int? = 0,
    @SerializedName("video")
    val video: Boolean? = false,
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0
)