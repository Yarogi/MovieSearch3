package com.practicum.mymovies.ui.cast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.mymovies.databinding.ActivityMoviesCastBinding
import com.practicum.mymovies.presentation.cast.MovieCastViewModel
import com.practicum.mymovies.presentation.cast.MoviesCastRVItem
import com.practicum.mymovies.presentation.cast.MoviesCastState
import com.practicum.mymovies.util.invisible
import com.practicum.mymovies.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MoviesCastActivity : AppCompatActivity() {

    private val moviesCastViewModel: MovieCastViewModel by viewModel {
        parametersOf(intent.getStringExtra(ARGS_MOVIE_ID))
    }

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate()
    )

    private lateinit var binding: ActivityMoviesCastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moviesCastRecyclerView.adapter = adapter
        binding.moviesCastRecyclerView.layoutManager = LinearLayoutManager(this)

        moviesCastViewModel.observeState().observe(this) {
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

    companion object {
        private const val ARGS_MOVIE_ID = "movie_id"

        fun newInstance(context: Context, movieId: String): Intent {
            return Intent(context, MoviesCastActivity::class.java).apply {
                putExtra(ARGS_MOVIE_ID, movieId)
            }
        }
    }

}