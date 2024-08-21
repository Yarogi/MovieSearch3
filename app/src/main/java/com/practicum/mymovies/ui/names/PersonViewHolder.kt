package com.practicum.mymovies.ui.names

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.models.Person

class PersonViewHolder(parrent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parrent.context).inflate(R.layout.list_item_person, parrent, false)
) {

    val photo: ImageView = itemView.findViewById(R.id.photo)
    val name: TextView = itemView.findViewById(R.id.name)
    val description: TextView = itemView.findViewById(R.id.description)

    fun bind(person: Person) {
        Glide.with(itemView)
            .load(person.photoUrl)
            .placeholder(R.drawable.ic_person)
            .circleCrop()
            .into(photo)

        name.text = person.title
        description.text = person.description
    }
}