package com.practicum.mymovies.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.models.Movie

class HistoryViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_history, parent, false)
    ) {

    var cover: ImageView = itemView.findViewById(R.id.cover)
    var title: TextView = itemView.findViewById(R.id.title)
    var description: TextView = itemView.findViewById(R.id.description)

    fun bind(movie: Movie) {
        Glide.with(itemView)
            .load(movie.image)
            .into(cover)

        title.text = movie.title
        description.text = movie.description
    }
}