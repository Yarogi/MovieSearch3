package com.practicum.mymovies.ui.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.mymovies.databinding.ActivityMoviesCastBinding
import com.practicum.mymovies.presentation.cast.MovieCastViewModel
import com.practicum.mymovies.presentation.cast.MoviesCastRVItem
import com.practicum.mymovies.presentation.cast.MoviesCastState
import com.practicum.mymovies.util.BindingFragment
import com.practicum.mymovies.util.invisible
import com.practicum.mymovies.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieCastFragment : BindingFragment<ActivityMoviesCastBinding>() {

    companion object {

        private const val ARGS_MOVIE_ID = "movie_id"

        fun newInstance(movieId: String): Fragment {

            val args = Bundle()
            args.putString(ARGS_MOVIE_ID, movieId)

            val fragment = MovieCastFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(requireArguments().getString(ARGS_MOVIE_ID) ?: "")
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate()
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): ActivityMoviesCastBinding {
        return ActivityMoviesCastBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(context)

        moviesCastViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is MoviesCastState.Content -> showContent(it)
                is MoviesCastState.Error -> showError(it)
                is MoviesCastState.Loading -> showLoading()
            }
        }

    }

    private fun showLoading() {
        binding.contentContainer.invisible()
        binding.errorMessageTextView.invisible()
        binding.progressBar.visibility
    }

    private fun showError(state: MoviesCastState.Error) {
        binding.contentContainer.invisible()
        binding.progressBar.invisible()

        binding.errorMessageTextView.visible()
        binding.errorMessageTextView.text = state.message
    }

    private fun showContent(state: MoviesCastState.Content) {
        binding.progressBar.invisible()
        binding.errorMessageTextView.invisible()

        binding.contentContainer.visible()

        binding.movieTitle.text = state.fullTitle
        adapter.items = state.items as List<MoviesCastRVItem>

        adapter.notifyDataSetChanged()
    }


}