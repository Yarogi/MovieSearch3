package com.practicum.mymovies.ui.movies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.mymovies.R
import com.practicum.mymovies.databinding.ActivityMoviesBinding
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.presentation.movies.MovieRVItem
import com.practicum.mymovies.presentation.movies.MoviesState
import com.practicum.mymovies.presentation.movies.MoviesViewModel
import com.practicum.mymovies.ui.details.DetailsFragment
import com.practicum.mymovies.util.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : BindingFragment<ActivityMoviesBinding>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = MoviesFragment()
    }

    private val adapter = ListDelegationAdapter(movieItemDelegate(
        object : MovieClickListener {
            override fun onMovieClick(movie: Movie) {

                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    add(
                        R.id.main_fragment_container_view,
                        DetailsFragment.newInstance(
                            poster = movie.image,
                            movieId = movie.id
                        )
                    )
                    addToBackStack(null)
                }

            }

            override fun onFavoriteToggleClick(movie: Movie) {
                viewModel.toggleFavorite(movie = movie)
            }

        }
    ))

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val viewModel by viewModel<MoviesViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): ActivityMoviesBinding {
        return ActivityMoviesBinding.inflate(inflater, container, false)
    }

    private lateinit var textWatcher: TextWatcher

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.moviesList.adapter = adapter


        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        textWatcher.let {
            binding.queryInput.addTextChangedListener(it)
        }

        viewModel.observeShowToast().observe(viewLifecycleOwner) { toast ->
            showToast(toast)
        }
        viewModel.observeState().observe(viewLifecycleOwner) { state -> render(state) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher.let {
            binding.queryInput.removeTextChangedListener(it)
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.message)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        binding.moviesList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.moviesList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        binding.moviesList.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.items = movies.map { movie -> MovieRVItem.MovieItem(movie) }
        adapter.notifyDataSetChanged()

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}