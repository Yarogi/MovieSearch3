package com.practicum.mymovies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.mymovies.R
import com.practicum.mymovies.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    companion object {

        private const val POSTER_KEY = "poster"
        private const val MOVIE_ID_KEY = "id"

        fun newInstance(poster: String, movieId: String): Fragment {

            val bundle = Bundle()
            bundle.putString(POSTER_KEY, poster)
            bundle.putString(MOVIE_ID_KEY, movieId)

            val newFragment = DetailsFragment()
            newFragment.arguments = bundle

            return newFragment

        }

        // Тег для использования во FragmentManager
        const val TAG = "DetailsFragment"
    }

    private lateinit var tabMediator: TabLayoutMediator


    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentDetailsBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentArg = requireArguments()
        val posterUrl = currentArg.getString(POSTER_KEY) ?: ""
        val movieId = currentArg.getString(MOVIE_ID_KEY) ?: ""


        binding.viewPager.adapter = DetailsViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            posterUrl = posterUrl,
            movieId = movieId
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.poster)
                else -> tab.text = getString(R.string.details)
            }
        }

        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }

}