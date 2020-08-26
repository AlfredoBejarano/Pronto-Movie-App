package me.alfredobejarano.prontomovieapp.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import me.alfredobejarano.prontomovieapp.databinding.FragmentMovieListBinding
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.viewBinding
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter.MovieViewHolder
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel
import javax.inject.Inject

/**
 * BaseListFragment
 */
abstract class BaseListFragment : Fragment() {
    @Inject
    lateinit var mediaPlayer: MediaPlayer

    private var updateListJob: Job? = null
    private val viewModel: MovieListViewModel by activityViewModels()
    private val binding by viewBinding(FragmentMovieListBinding::inflate)

    /**
     * Defines the adapter function to be called to update the RecyclerView with new items.
     */
    abstract fun movieListOperation(viewModel: MovieListViewModel, nextPage: Boolean = false): Job

    /**
     * Defines the message to show in the empty indicator
     */
    @StringRes
    abstract fun emptyListMessageResource(): Int

    /**
     * Draws the view for this fragment
     */
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    /**
     * Initializes the widgets inside this fragment view.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            movieListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            emptyListMessage.setText(emptyListMessageResource())
        }
        observeMovieListChanges()
        getMovies()
    }

    /**
     * Cancels the update list job view.
     */
    override fun onDestroy() {
        super.onDestroy()
        cancelUpdateListJob()
    }

    /**
     * Observes changes within the movie list.
     */
    private fun observeMovieListChanges() =
        viewModel.movieListLiveData.observe(viewLifecycleOwner, Observer(::updateMovieList))

    /**
     * Observes updates to the list of movies from the movie list operation.
     */
    private fun updateMovieList(newMovies: List<Movie>) {
        getMovieListAdapter()?.updateMovies(newMovies)?.let {
            updateListJob = it
        } ?: run {
            createMovieListAdapter(newMovies)
        }
        showEmptyListState(newMovies.isEmpty())
    }

    /**
     * Shows or hides the empty view if the movie list is empty.
     */
    private fun showEmptyListState(emptyList: Boolean) = binding.run {
        movieListRecyclerView.visibility = if (emptyList) View.GONE else View.VISIBLE
        emptyListMessage.visibility = if (emptyList) View.VISIBLE else View.GONE
    }

    /**
     * Creates the adapter for the movie RecyclerView.
     */
    private fun createMovieListAdapter(movies: List<Movie>) {
        binding.movieListRecyclerView.adapter =
            MovieListAdapter(movies, ::onLastItem, ::updateFavorites)
    }

    /**
     * Updates the favorites list with the given movie item.
     */
    private fun updateFavorites(position: Int, item: Movie) =
        viewModel.reportFavoriteMovie(item).observe(viewLifecycleOwner, Observer {
            it?.run {
                mediaPlayer.start()
                onItemClick(position, item)
            }
        })

    /**
     * Cancels the update list job.
     */
    private fun cancelUpdateListJob() {
        if (updateListJob?.isActive == true) {
            updateListJob?.cancel()
        }
        updateListJob = null
    }

    /**
     * Retrieves the adapter from the RecyclerView widget.
     */
    protected fun getMovieListAdapter() = binding.movieListRecyclerView.adapter as? MovieListAdapter

    /**
     * Called when the last item on the view gets bind.
     */
    protected open fun onLastItem() = Unit

    /**
     * Called when a movie item has been clicked.
     */
    protected open fun onItemClick(position: Int, item: Movie) = Unit

    /**
     * Retrieves the list of movies using the given movie list operation for this fragment.
     */
    protected fun getMovies(nextPage: Boolean = false) {
        updateListJob = movieListOperation(viewModel, nextPage)
    }

    /**
     * Retrieves the ViewHolder from the RecyclerView at the given position.
     */
    protected fun getViewHolderAtPosition(position: Int) =
        binding.movieListRecyclerView.findViewHolderForAdapterPosition(position) as? MovieViewHolder
}