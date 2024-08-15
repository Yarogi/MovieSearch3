package com.practicum.mymovies.ui.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.mymovies.databinding.FragmentMoviesCastBinding
import com.practicum.mymovies.presentation.cast.MovieCastViewModel
import com.practicum.mymovies.presentation.cast.MoviesCastRVItem
import com.practicum.mymovies.presentation.cast.MoviesCastState

import com.practicum.mymovies.util.invisible
import com.practicum.mymovies.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieCastFragment : Fragment() {

    companion object {

        private const val ARGS_MOVIE_ID = "movie_id"

        fun newInstance(movieId: String): Fragment {

            return MovieCastFragment().apply {
                arguments = bundleOf(ARGS_MOVIE_ID to movieId)
            }

        }
    }

    private var _binding: FragmentMoviesCastBinding? = null
    private val binding get() = _binding!!

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(requireArguments().getString(ARGS_MOVIE_ID) ?: "")
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMoviesCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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