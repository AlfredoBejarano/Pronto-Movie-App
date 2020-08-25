package me.alfredobejarano.prontomovieapp.model.remote

import com.google.gson.annotations.SerializedName

/**
 * MovieListResult
 *
 * Defines the result of a Movie List query.
 */
data class MovieListResult(
    @SerializedName("page")
    val page: Int? = 1,
    @SerializedName("results")
    val results: List<MovieListResultObject>? = emptyList(),
    @SerializedName("total_results")
    val total_results: Int? = 0,
    @SerializedName("total_pages")
    val total_pages: Int? = 1
)