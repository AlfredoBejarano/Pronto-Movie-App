package me.alfredobejarano.prontomovieapp.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.Job
import me.alfredobejarano.prontomovieapp.injection.ViewModelFactory
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.viewBinding
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter.MovieViewHolder
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel
import javax.inject.Inject

/**
 * BaseListFragment
 */
abstract class BaseListFragment<V : ViewBinding> : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    private var updateListJob: Job? = null
    private val binding by viewBinding(::buildViewBinding)
    private val viewModel: MovieListViewModel by activityViewModels { viewModelFactory }

    abstract fun getRecyclerView(binding: V): RecyclerView

    /**
     * Builds the ViewBinding object for this fragment class.
     */
    abstract fun buildViewBinding(inflater: LayoutInflater): V

    /**
     * Defines the adapter function to be called to update the RecyclerView with new items.
     */
    abstract fun movieListOperation(viewModel: MovieListViewModel, nextPage: Boolean = false): Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(binding).layoutManager = LinearLayoutManager(requireContext())
        observeMovieListChanges()
        getMovies()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelUpdateListJob()
    }

    private fun observeMovieListChanges() =
        viewModel.movieListLiveData.observe(viewLifecycleOwner, Observer {
            it?.run { updateMovieList(getMovieListAdapter(), it) }
        })

    private fun updateMovieList(adapter: RecyclerView.Adapter<*>?, newMovies: List<Movie>) =
        getMovieListAdapter()?.updateMovies(newMovies)?.let {
            updateListJob = it
        } ?: run {
            createMovieListAdapter(newMovies)
        }

    private fun createMovieListAdapter(movies: List<Movie>) {
        getRecyclerView(binding).adapter = MovieListAdapter(movies, ::onLastItem, ::updateFavorites)
    }

    private fun updateFavorites(position: Int, item: Movie) =
        viewModel.reportFavoriteMovie(item).observe(viewLifecycleOwner, Observer {
            onItemClick(position, item)
        })

    private fun cancelUpdateListJob() {
        if (updateListJob?.isActive == true) {
            updateListJob?.cancel()
        }
        updateListJob = null
    }

    protected fun getMovieListAdapter() =
        getRecyclerView(binding).adapter as? MovieListAdapter

    protected open fun onLastItem() = Unit

    protected open fun onItemClick(position: Int, item: Movie) = Unit

    protected fun getMovies(nextPage: Boolean = false) {
        updateListJob = movieListOperation(viewModel, nextPage)
    }

    protected fun getViewHolderAtPosition(position: Int) =
        getRecyclerView(binding).findViewHolderForAdapterPosition(position) as? MovieViewHolder
}