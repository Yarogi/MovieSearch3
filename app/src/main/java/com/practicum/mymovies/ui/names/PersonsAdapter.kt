package com.practicum.mymovies.ui.names

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.mymovies.domain.models.Person

class PersonsAdapter : RecyclerView.Adapter<PersonViewHolder>() {

    var persons = mutableListOf<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(parent)

    override fun getItemCount(): Int = persons.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(persons[position])
    }

}