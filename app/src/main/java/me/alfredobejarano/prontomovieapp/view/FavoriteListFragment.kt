package me.alfredobejarano.prontomovieapp.view

import dagger.hilt.android.AndroidEntryPoint
import me.alfredobejarano.prontomovieapp.R
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel

/**
 * FavoriteListFragment
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
@AndroidEntryPoint
class FavoriteListFragment : BaseListFragment() {

    override fun emptyListMessageResource() = R.string.favorites_empty

    override fun onItemClick(position: Int, item: Movie) = getMovies()

    override fun movieListOperation(viewModel: MovieListViewModel, nextPage: Boolean) =
        viewModel.getFavorites()
}