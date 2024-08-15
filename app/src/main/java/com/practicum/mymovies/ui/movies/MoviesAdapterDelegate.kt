package com.practicum.mymovies.ui.movies

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.practicum.mymovies.R
import com.practicum.mymovies.core.ui.RVItem
import com.practicum.mymovies.databinding.ListItemMovieBinding
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.presentation.movies.MovieRVItem

fun movieItemDelegate(clickListener: MovieClickListener) =
    adapterDelegateViewBinding<MovieRVItem.MovieItem, RVItem, ListItemMovieBinding>(
        { layoutInflater, root -> ListItemMovieBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {

            val movie = item.movie

            //Картинка
            Glide.with(binding.root)
                .load(movie.image)
                .into(binding.cover)

            binding.titleValue.text = movie.title
            binding.description.text = movie.description

            binding.favorite.setImageDrawable(
                context.getDrawable(
                    if (movie.inFavorite) R.drawable.active_favorite else R.drawable.inactive_favorite
                )
            )

            binding.root.setOnClickListener { clickListener.onMovieClick(movie) }
            binding.favorite.setOnClickListener { clickListener.onFavoriteToggleClick(movie) }

        }
    }

interface MovieClickListener {
    fun onMovieClick(movie: Movie)
    fun onFavoriteToggleClick(movie: Movie)
}