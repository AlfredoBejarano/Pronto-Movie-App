package me.alfredobejarano.prontomovieapp.viewmodel

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import me.alfredobejarano.prontomovieapp.domain.GetMovieListUseCase
import javax.inject.Inject

/**
 * MovieListViewModel
 */
class MovieListViewModel @Inject constructor(private val getMoviesListUseCase: GetMovieListUseCase) {
    fun getMovieList(includeAdultMovies: Boolean = false) = liveData(Dispatchers.IO) {
        emit(getMoviesListUseCase.getMovieList(includeAdultMovies))
    }
}