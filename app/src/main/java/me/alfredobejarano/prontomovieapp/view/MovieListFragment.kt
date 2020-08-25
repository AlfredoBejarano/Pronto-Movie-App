package me.alfredobejarano.prontomovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import me.alfredobejarano.prontomovieapp.databinding.FragmentMovieListBinding
import me.alfredobejarano.prontomovieapp.utils.viewBinding
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel

/**
 * MovieListFragment
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class MovieListFragment : Fragment() {
    private val viewModel by viewModels<MovieListViewModel>()
    private val binding: FragmentMovieListBinding by viewBinding(FragmentMovieListBinding::inflate)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            movieListSwipeRefreshLayout.setOnRefreshListener { getMovies() }
            movieListRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), MOVIE_LIST_GRID_SPAN)
        }
    }

    private fun getMovies() = viewModel.getMovieList().observe(viewLifecycleOwner, Observer {
        it?.let { safeMovieList ->
            binding.movieListRecyclerView.adapter?.let { adapter ->
                // TODO - Update Adapter.
            } ?: run {
                // TODO - Create Adapter
            }
        }
    })

    private companion object {
        const val MOVIE_LIST_GRID_SPAN = 2
    }
}