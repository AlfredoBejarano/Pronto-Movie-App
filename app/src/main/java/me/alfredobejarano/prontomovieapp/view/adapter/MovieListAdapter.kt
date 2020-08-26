package me.alfredobejarano.prontomovieapp.view.adapter

import android.R.anim.slide_in_left
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.prontomovieapp.R.anim.animation_pop
import me.alfredobejarano.prontomovieapp.R.drawable.ic_favorite_black_24dp
import me.alfredobejarano.prontomovieapp.R.drawable.ic_favorite_border_black_24dp
import me.alfredobejarano.prontomovieapp.databinding.ItemMovieBinding
import me.alfredobejarano.prontomovieapp.model.local.Movie

/**
 * MovieListAdapter
 */
class MovieListAdapter(
    private var movies: List<Movie>,
    private val onLastItem: () -> Unit = {},
    private val onMovieFavoriteClicked: (Int, Movie) -> Unit
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        onLastItem,
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onMovieFavoriteClicked
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(position, movies[position], itemCount - 1)

    fun updateMovies(newMovies: List<Movie>) = GlobalScope.launch(IO) {
        val callback = MovieDiffCallback(movies, newMovies)
        val result = DiffUtil.calculateDiff(callback)
        launch(Main) {
            result.dispatchUpdatesTo(this@MovieListAdapter)
            movies = newMovies
        }
    }

    fun updateMovieAtPosition(viewHolder: MovieViewHolder?, movie: Movie) =
        viewHolder?.updateIcon(movie)

    class MovieDiffCallback(
        private val oldMovies: List<Movie>,
        private val newMovies: List<Movie>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldMovies.size

        override fun getNewListSize() = newMovies.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMovies[oldItemPosition].compareTo(newMovies[newItemPosition]) == 0

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldMovies[oldItemPosition].isFavorite == newMovies[newItemPosition].isFavorite
    }

    class MovieViewHolder(
        private val onLastItem: () -> Unit,
        private val binding: ItemMovieBinding,
        private val onMovieFavoriteClicked: (Int, Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, item: Movie, maxItems: Int) = binding.run {
            movie = item
            imageViewMovieFavorite.setOnClickListener {
                onMovieFavoriteClicked(position, item.apply { isFavorite = !isFavorite })
            }
            binding.root.startAnimation(loadAnimation(itemView.context, slide_in_left))
            if (position == maxItems) onLastItem()
        }

        fun updateIcon(movie: Movie) = binding.imageViewMovieFavorite.run {
            setImageResource(
                if (movie.isFavorite) {
                    ic_favorite_black_24dp
                } else {
                    ic_favorite_border_black_24dp
                }
            )
            startAnimation(loadAnimation(itemView.context, animation_pop))
        }
    }
}