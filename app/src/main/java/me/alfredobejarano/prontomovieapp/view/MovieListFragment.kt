package me.alfredobejarano.prontomovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.Job
import me.alfredobejarano.prontomovieapp.databinding.FragmentMovieListBinding
import me.alfredobejarano.prontomovieapp.injection.ViewModelFactory
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.EventManager
import me.alfredobejarano.prontomovieapp.utils.EventManager.requestNextPageLiveData
import me.alfredobejarano.prontomovieapp.utils.viewBinding
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter.MovieViewHolder
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel
import javax.inject.Inject

/**
 * MovieListFragment
 */
class MovieListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var updateListJob: Job? = null
    private val viewModel by viewModels<MovieListViewModel> { viewModelFactory }
    private val binding: FragmentMovieListBinding by viewBinding(FragmentMovieListBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        movieListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieListSwipeRefreshLayout.setOnRefreshListener { getMovies() }

        viewModel.movieListLiveData.observe(viewLifecycleOwner, Observer {
            it?.run { updateMovieList(binding.movieListRecyclerView.adapter, this) }
        })

        requestNextPageLiveData.observe(viewLifecycleOwner, Observer { getMovies(true) })

        getMovies()
    }

    private fun updateMovieList(adapter: RecyclerView.Adapter<*>?, newMovies: List<Movie>) {
        (adapter as? MovieListAdapter)?.updateMovies(newMovies)?.let {
            updateListJob = it
        } ?: run {
            createMovieListAdapter(newMovies)
        }
        binding.movieListSwipeRefreshLayout.isRefreshing = false
    }

    private fun createMovieListAdapter(movies: List<Movie>) {
        binding.movieListRecyclerView.adapter = MovieListAdapter(movies, ::onMovieIconClick)
    }

    private fun onMovieIconClick(position: Int, movie: Movie) =
        viewModel.reportFavoriteMovie(movie).observe(viewLifecycleOwner, Observer {
            if (movie.isFavorite) EventManager.requestFavoriteSoundPlay()
            updateMovieAtPosition(position, movie)
        })

    private infix fun getMovieViewHolderAt(position: Int) =
        binding.movieListRecyclerView.findViewHolderForAdapterPosition(position) as? MovieViewHolder

    private fun updateMovieAtPosition(position: Int, movie: Movie) =
        (binding.movieListRecyclerView.adapter as? MovieListAdapter)?.updateMovieAtPosition(
            getMovieViewHolderAt(position),
            movie
        )

    private fun getMovies(nextPage: Boolean = false) {
        updateListJob = viewModel.getMovieList(nextPage = nextPage)
    }

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
}