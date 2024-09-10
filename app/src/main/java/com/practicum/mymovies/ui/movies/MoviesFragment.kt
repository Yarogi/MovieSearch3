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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.mymovies.R
import com.practicum.mymovies.core.ui.RVItem
import com.practicum.mymovies.databinding.FragmentMoviesBinding
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.presentation.movies.MovieRVItem
import com.practicum.mymovies.presentation.movies.MoviesState
import com.practicum.mymovies.presentation.movies.MoviesViewModel
import com.practicum.mymovies.ui.RootActivity
import com.practicum.mymovies.ui.details.DetailsFragment
import com.practicum.mymovies.util.debounce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<MoviesViewModel>()

    private var isClickAllowed = true

    private lateinit var textWatcher: TextWatcher

    //debouncer нажатия
    private lateinit var onMovieClickDebounce: (Movie) -> Unit

    //Анимация
    private var adapter: ListDelegationAdapter<List<RVItem>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Debouncer init
        onMovieClickDebounce = debounce<Movie>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { movie ->
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                DetailsFragment.createArgs(movie.id, movie.image)
            )
        }
        //
        adapter = ListDelegationAdapter(movieItemDelegate(
            object : MovieClickListener {
                override fun onMovieClick(movie: Movie) {
                    (activity as RootActivity).animateBottomNavigationView()
                    onMovieClickDebounce(movie)
                }

                override fun onFavoriteToggleClick(movie: Movie) {
                    viewModel.toggleFavorite(movie = movie)
                }

            }
        ))
        //

        binding.moviesList.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
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
        adapter = null
        binding.moviesList.adapter = null
        _binding = null
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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

        adapter?.items = movies.map { movie -> MovieRVItem.MovieItem(movie) }
        adapter?.notifyDataSetChanged()

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {

            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
        return current
    }

}