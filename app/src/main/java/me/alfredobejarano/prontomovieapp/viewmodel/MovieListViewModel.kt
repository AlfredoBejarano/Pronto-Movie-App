package me.alfredobejarano.prontomovieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import me.alfredobejarano.prontomovieapp.domain.GetMovieListUseCase
import me.alfredobejarano.prontomovieapp.domain.UpdateMovieUseCase
import me.alfredobejarano.prontomovieapp.model.local.Movie
import javax.inject.Inject

/**
 * MovieListViewModel
 */
class MovieListViewModel @Inject constructor(
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val getMoviesListUseCase: GetMovieListUseCase
) : ViewModel() {
    private var movies = emptyList<Movie>()

    private val _movieListLiveData = MutableLiveData<List<Movie>>()
    val movieListLiveData = _movieListLiveData as LiveData<List<Movie>>

    fun getMovieList(includeAdultMovies: Boolean = false, nextPage: Boolean = false) =
        viewModelScope.launch(IO) {
            val newMovieList = getMoviesListUseCase.getMovieList(includeAdultMovies, nextPage)
            movies = if (nextPage) {
                movies.toMutableList().apply { addAll(newMovieList) }
            } else {
                newMovieList
            }
            _movieListLiveData.postValue(movies)
        }

    fun reportFavoriteMovie(movie: Movie) = liveData(IO) {
        emit(updateMovieUseCase.updateMovie(movie))
    }
}