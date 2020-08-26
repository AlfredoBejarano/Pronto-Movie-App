package me.alfredobejarano.prontomovieapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.alfredobejarano.prontomovieapp.domain.FetchFavoriteMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.GetMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.UpdateMovieUseCase
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.execute
import me.alfredobejarano.prontomovieapp.utils.executeForLiveData
import javax.inject.Inject

/**
 * MovieListViewModel
 */
class MovieListViewModel @ViewModelInject constructor(
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val getMoviesListUseCase: GetMovieListUseCase,
    private val fetchFavoritesUseCase: FetchFavoriteMovieListUseCase
) : ViewModel() {
    private var movies = emptyList<Movie>()
    private val _movieListLiveData = MutableLiveData<List<Movie>>()
    val movieListLiveData = _movieListLiveData as LiveData<List<Movie>>

    fun getMovieList(includeAdultMovies: Boolean = false, nextPage: Boolean = false) = execute {
        val newMovieList = getMoviesListUseCase.getMovieList(includeAdultMovies, nextPage)
        movies = if (nextPage) {
            movies.toMutableList().apply { addAll(newMovieList) }
        } else {
            newMovieList
        }
        _movieListLiveData.postValue(movies)
    }

    fun reportFavoriteMovie(movie: Movie) = executeForLiveData {
        updateMovieUseCase.updateMovie(movie)
    }

    fun getFavorites() = execute {
        _movieListLiveData.postValue(fetchFavoritesUseCase.fetchFavorites())
    }
}