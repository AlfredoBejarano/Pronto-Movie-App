package me.alfredobejarano.prontomovieapp.view

import android.view.LayoutInflater
import me.alfredobejarano.prontomovieapp.databinding.FragmentMovieListBinding
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel

/**
 * MovieListFragment
 */
class MovieListFragment : BaseListFragment<FragmentMovieListBinding>() {
    override fun movieListOperation(viewModel: MovieListViewModel, nextPage: Boolean) =
        viewModel.getMovieList(nextPage = nextPage)

    override fun buildViewBinding(inflater: LayoutInflater): FragmentMovieListBinding =
        FragmentMovieListBinding.inflate(inflater)

    override fun getRecyclerView(binding: FragmentMovieListBinding) = binding.movieListRecyclerView

    override fun onLastItem() = getMovies(true)

    override fun onItemClick(position: Int, item: Movie) {
        getMovieListAdapter()?.updateMovieAtPosition(getViewHolderAtPosition(position), item)
    }

    override fun getEmptyListMessageView(binding: FragmentMovieListBinding) =
        binding.emptyListMessage
}