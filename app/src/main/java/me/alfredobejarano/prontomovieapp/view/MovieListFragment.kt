package me.alfredobejarano.prontomovieapp.view

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
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

    override fun getRecyclerView(binding: FragmentMovieListBinding): RecyclerView =
        binding.movieListRecyclerView

    override fun onLastItem() = getMovies(true)

    override fun onItemClick(position: Int, item: Movie) {
        if (item.isFavorite) mediaPlayer.start()
        getMovieListAdapter()?.updateMovieAtPosition(getViewHolderAtPosition(position), item)
    }
}