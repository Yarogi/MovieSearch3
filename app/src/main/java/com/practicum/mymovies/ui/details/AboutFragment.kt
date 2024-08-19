package com.practicum.mymovies.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.practicum.mymovies.R
import com.practicum.mymovies.core.navigation.Router
import com.practicum.mymovies.databinding.FragmentAboutBinding
import com.practicum.mymovies.domain.models.MovieDetails
import com.practicum.mymovies.presentation.details.AboutState
import com.practicum.mymovies.presentation.details.AboutViewModel
import com.practicum.mymovies.ui.cast.MovieCastFragment
import com.practicum.mymovies.util.invisible
import com.practicum.mymovies.util.visible
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AboutFragment : Fragment() {

    companion object {
        private const val MOVIE_ID = "movieId"

        fun newInstance(movieId: String) = AboutFragment().apply {
            arguments = createArgs(movieId)
        }

        fun createArgs(movieId: String): Bundle {
            return bundleOf(MOVIE_ID to movieId)
        }
    }

    private val aboutViewModel: AboutViewModel by viewModel {
        parametersOf(requireArguments().getString(MOVIE_ID))
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is AboutState.Content -> showDetails(it.movie)
                is AboutState.Error -> showErrorMessage(it.message)
            }
        }

        binding.showCastButton.setOnClickListener {


            findNavController().navigate(
                R.id.action_detailsFragment_to_movieCastFragment,
                MovieCastFragment.createArguments(
                    requireArguments().getString(MOVIE_ID).orEmpty()
                )
            )

        }
    }

    private fun showErrorMessage(message: String) {
        binding.apply {
            details.invisible()
            errorMessage.visible()
            errorMessage.text = message
        }
    }

    private fun showDetails(movieDetails: MovieDetails) {
        binding.apply {
            details.visible()
            errorMessage.invisible()
            titleValue.text = movieDetails.title
            ratingValue.text = movieDetails.imDbRating
            yearValue.text = movieDetails.year
            countryValue.text = movieDetails.countries
            genreValue.text = movieDetails.genres
            directorValue.text = movieDetails.directors
            writerValue.text = movieDetails.writers
            castValue.text = movieDetails.stars
            plot.text = movieDetails.plot
        }
    }

}