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
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.practicum.mymovies.R
import com.practicum.mymovies.databinding.FragmentMoviesBinding
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.presentation.movies.MovieRVItem
import com.practicum.mymovies.presentation.movies.MoviesState
import com.practicum.mymovies.presentation.movies.MoviesViewModel
import com.practicum.mymovies.ui.details.DetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = MoviesFragment()
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<MoviesViewModel>()

    private val adapter = ListDelegationAdapter(movieItemDelegate(
        object : MovieClickListener {
            override fun onMovieClick(movie: Movie) {

                if (clickDebounce()) {

                    parentFragmentManager.commit {
                        replace(
                            //Указали, в каком контейнере работаем
                            R.id.rootFragmentContainerView,
                            //Создали фрагмент
                            DetailsFragment.newInstance(
                                poster = movie.image,
                                movieId = movie.id
                            ),
                            //Указали тэг фрагмента
                            DetailsFragment.TAG
                        )
                        addToBackStack(DetailsFragment.TAG)
                    }

                }
            }

            override fun onFavoriteToggleClick(movie: Movie) {
                viewModel.toggleFavorite(movie = movie)
            }

        }
    ))

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    private lateinit var textWatcher: TextWatcher

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