package com.practicum.mymovies.ui.cast

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.practicum.mymovies.core.ui.RVItem
import com.practicum.mymovies.databinding.ListItemCastBinding
import com.practicum.mymovies.databinding.ListItemHeaderBinding
import com.practicum.mymovies.presentation.cast.MoviesCastRVItem
import com.practicum.mymovies.util.invisible
import com.practicum.mymovies.util.visible

fun movieCastHeaderDelegate() =
    adapterDelegateViewBinding<MoviesCastRVItem.HeaderItem, RVItem, ListItemHeaderBinding>(
        { layoutInflater, root -> ListItemHeaderBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.headerTextView.text = item.headerText
        }
    }

fun movieCastPersonDelegate() =
    adapterDelegateViewBinding<MoviesCastRVItem.PersonItem, RVItem, ListItemCastBinding>(
        { layoutInflater, root -> ListItemCastBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            if (item.data.image == null) {
                binding.actorImageView.invisible()
            } else {
                Glide.with(itemView)
                    .load(item.data.image)
                    .into(binding.actorImageView)
                binding.actorImageView.visible()
            }

            binding.actorNameTextView.text = item.data.name
            binding.actorDescriptionTextView.text = item.data.description
        }
    }