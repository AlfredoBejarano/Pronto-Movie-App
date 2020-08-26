package me.alfredobejarano.prontomovieapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import me.alfredobejarano.prontomovieapp.databinding.FragmentMovieListBinding
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.viewBinding
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel

/**
 * MovieListFragment
 */
class MovieListFragment : Fragment() {
    private var updateListJob: Job? = null
    private val viewModel by viewModels<MovieListViewModel>()
    private val binding: FragmentMovieListBinding by viewBinding(FragmentMovieListBinding::inflate)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        movieListRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), MOVIE_LIST_GRID_SPAN)
        movieListSwipeRefreshLayout.setOnRefreshListener { getMovies() }
    }

    private fun updateMovieList(adapter: RecyclerView.Adapter<*>?, newMovies: List<Movie>) =
        (adapter as? MovieListAdapter)?.updateMovies(newMovies)?.let {
            updateListJob = it
        } ?: run {
            createMovieListAdapter(newMovies)
        }

    private fun createMovieListAdapter(movies: List<Movie>) {
        binding.movieListRecyclerView.adapter = MovieListAdapter(movies) { movie ->
            // TODO - Add movie to favorites.
            Log.d("Movie", movie.title)
        }
    }

    private fun getMovies() = viewModel.getMovieList().observe(viewLifecycleOwner, Observer {
        it?.run { updateMovieList(binding.movieListRecyclerView.adapter, this) }
    })

    override fun onDestroyView() {
        super.onDestroyView()
        cancelUpdateListJob()
    }

    private fun cancelUpdateListJob() {
        if (updateListJob?.isActive == true) {
            updateListJob?.cancel()
        }
        updateListJob = null
    }

    private companion object {
        const val MOVIE_LIST_GRID_SPAN = 2
    }
}