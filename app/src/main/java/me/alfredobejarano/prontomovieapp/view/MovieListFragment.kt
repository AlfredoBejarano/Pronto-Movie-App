package me.alfredobejarano.prontomovieapp.view

import dagger.hilt.android.AndroidEntryPoint
import me.alfredobejarano.prontomovieapp.R
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel

/**
 * MovieListFragment
 */
@AndroidEntryPoint
class MovieListFragment : BaseListFragment() {

    override fun onLastItem() = getMovies(true)

    override fun emptyListMessageResource() = R.string.movie_list_empty

    override fun movieListOperation(viewModel: MovieListViewModel, nextPage: Boolean) =
        viewModel.getMovieList(nextPage = nextPage)

    override fun onItemClick(position: Int, item: Movie) {
        getMovieListAdapter()?.updateMovieAtPosition(getViewHolderAtPosition(position), item)
    }
}