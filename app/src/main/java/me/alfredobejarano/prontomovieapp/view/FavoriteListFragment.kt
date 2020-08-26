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
        observeListChanges()
        getFavorites()
    }

    private fun getFavorites() = viewModel.getFavorites()

    private fun observeListChanges() =
        viewModel.movieListLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                binding.favoritesList.visibility = View.GONE
                binding.emptyListMessage.visibility = View.VISIBLE
            } else {
                binding.favoritesList.visibility = View.VISIBLE
                binding.emptyListMessage.visibility = View.GONE
                binding.favoritesList.adapter = MovieListAdapter(it, {}) { _, movie ->
                    viewModel.reportFavoriteMovie(movie).observe(viewLifecycleOwner, Observer {
                        EventManager.requestFavoriteSoundPlay()
                        getFavorites()
                    })
                }
            }
        })
}