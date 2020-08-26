package me.alfredobejarano.prontomovieapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.prontomovieapp.databinding.ItemMovieBinding
import me.alfredobejarano.prontomovieapp.model.local.Movie

/**
 * MovieListAdapter
 *
 * @author (c) AlfredoBejarano - alfredo.corona@rappi.com
 */
class MovieListAdapter(
    private var movies: List<Movie>,
    private val onMovieFavoriteClicked: (Movie) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onMovieFavoriteClicked
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder bind movies[position]

    fun updateMovies(newMovies: List<Movie>) = GlobalScope.launch(Dispatchers.IO) {
        val callback = MovieDiffCallback(movies, newMovies)
        val result = DiffUtil.calculateDiff(callback)
        launch(Dispatchers.Main) {
            result.dispatchUpdatesTo(this@MovieListAdapter)
            movies = newMovies
        }
    }

    class MovieDiffCallback(
        private val oldMovies: List<Movie>,
        private val newMovies: List<Movie>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldMovies.size

        override fun getNewListSize() = newMovies.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMovies[oldItemPosition].compareTo(newMovies[newItemPosition]) == 0

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieFavoriteClicked: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        infix fun bind(item: Movie) = binding.run {
            movie = item
            imageViewMovieFavorite.setOnClickListener { onMovieFavoriteClicked(item) }
        }
    }
}