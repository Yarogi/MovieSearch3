package com.practicum.mymovies.ui.names

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.mymovies.databinding.FragmentNamesBinding

class NamesFragment : Fragment() {

    private lateinit var binding: FragmentNamesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

}