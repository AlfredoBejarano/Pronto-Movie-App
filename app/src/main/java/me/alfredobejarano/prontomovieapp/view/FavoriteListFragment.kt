package me.alfredobejarano.prontomovieapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import me.alfredobejarano.prontomovieapp.databinding.FragmentFavoriteListBinding
import me.alfredobejarano.prontomovieapp.injection.ViewModelFactory
import me.alfredobejarano.prontomovieapp.model.local.Movie
import me.alfredobejarano.prontomovieapp.utils.EventManager
import me.alfredobejarano.prontomovieapp.utils.viewBinding
import me.alfredobejarano.prontomovieapp.view.adapter.MovieListAdapter
import me.alfredobejarano.prontomovieapp.viewmodel.MovieListViewModel
import javax.inject.Inject

/**
 * FavoriteListFragment
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class FavoriteListFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel: MovieListViewModel by activityViewModels { factory }
    private val binding by viewBinding(FragmentFavoriteListBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoritesList.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getFavorites().observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.emptyListMessage.visibility = View.VISIBLE
            } else {
                binding.emptyListMessage.visibility = View.GONE
                binding.favoritesList.adapter = MovieListAdapter(it, ::onMovieClick)
            }
        })
    }

    private fun onMovieClick(position: Int, movie: Movie) =
        viewModel.reportFavoriteMovie(movie).observe(viewLifecycleOwner, Observer {
            (binding.favoritesList.adapter as? MovieListAdapter)?.run {
                removeItemAtPosition(position)
                EventManager.requestFavoriteSoundPlay()
                binding.emptyListMessage.visibility = if (itemCount == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })
}