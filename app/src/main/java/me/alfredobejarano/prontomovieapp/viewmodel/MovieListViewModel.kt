package me.alfredobejarano.prontomovieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import me.alfredobejarano.prontomovieapp.domain.FetchFavoriteMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.GetMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.UpdateMovieUseCase
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.EventManager.showLoading
import javax.inject.Inject

/**
 * MovieListViewModel
 */
class MovieListViewModel @Inject constructor(
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val getMoviesListUseCase: GetMovieListUseCase,
    private val fetchFavoritesUseCase: FetchFavoriteMovieListUseCase
) : ViewModel() {
    private var movies = emptyList<Movie>()
    private val _movieListLiveData = MutableLiveData<List<Movie>>()
    val movieListLiveData = _movieListLiveData as LiveData<List<Movie>>

    fun getMovieList(includeAdultMovies: Boolean = false, nextPage: Boolean = false) =
        viewModelScope.launch(IO) {
            showLoading(true)
            val newMovieList = getMoviesListUseCase.getMovieList(includeAdultMovies, nextPage)
            movies = if (nextPage) {
                movies.toMutableList().apply { addAll(newMovieList) }
            } else {
                newMovieList
            }
            _movieListLiveData.postValue(movies)
        }

    fun reportFavoriteMovie(movie: Movie) = liveData(IO) {
        showLoading(true)
        emit(updateMovieUseCase.updateMovie(movie))
        showLoading(false)
    }

    fun getFavorites() = viewModelScope.launch(IO) {
        showLoading(true)
        _movieListLiveData.postValue(fetchFavoritesUseCase.fetchFavorites())
    }
}