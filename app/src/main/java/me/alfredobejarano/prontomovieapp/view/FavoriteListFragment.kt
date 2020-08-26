package me.alfredobejarano.prontomovieapp.view

import android.view.LayoutInflater
import me.alfredobejarano.prontomovieapp.databinding.FragmentFavoriteListBinding
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel

/**
 * FavoriteListFragment
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class FavoriteListFragment : BaseListFragment<FragmentFavoriteListBinding>() {
    override fun onItemClick(position: Int, item: Movie) = getMovies()

    override fun getRecyclerView(binding: FragmentFavoriteListBinding) =
        binding.favoritesList

    override fun buildViewBinding(inflater: LayoutInflater): FragmentFavoriteListBinding =
        FragmentFavoriteListBinding.inflate(inflater)

    override fun movieListOperation(viewModel: MovieListViewModel, nextPage: Boolean) =
        viewModel.getFavorites()

    override fun getEmptyListMessageView(binding: FragmentFavoriteListBinding) =
        binding.emptyListMessage
}