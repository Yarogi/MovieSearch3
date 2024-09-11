package com.practicum.mymovies.presentation.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.api.MoviesInteractor
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.util.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val moviesInteractor: MoviesInteractor,
    application: Application,
) : AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = mediatorStateLiveData

    private val mediatorStateLiveData = MediatorLiveData<MoviesState>().also { liveData ->
        liveData.addSource(stateLiveData) { movieState ->
            liveData.value = when (movieState) {
                is MoviesState.Content -> MoviesState.Content(movieState.movies.sortedByDescending { it.inFavorite })
                is MoviesState.Empty -> movieState
                is MoviesState.Error -> movieState
                is MoviesState.Loading -> movieState
            }
        }
    }

    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast

    private var latestSearchText: String? = null

    private val searchMovieDebounce: (String) -> Unit by lazy {
        debounce<String>(
            SEARCH_DEBOUNCE_DELAY,
            viewModelScope,
            true
        ) { text -> searchRequest(text) }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        searchMovieDebounce(changedText)

    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            viewModelScope.launch {
                moviesInteractor
                    .searchMovies(newSearchText)
                    .collect { pair -> processResultSearch(pair.first, pair.second) }
            }
        }
    }

    private fun processResultSearch(foundMovies: List<Movie>?, errorMessage: String?) {
        val movies = mutableListOf<Movie>()
        if (foundMovies != null) {
            movies.addAll(foundMovies)
        }

        when {
            errorMessage != null -> {
                renderState(
                    MoviesState.Error(
                        message = getApplication<Application>().getString(R.string.something_went_wrong),
                    )
                )
                showToast.postValue(errorMessage ?: "")
            }

            movies.isEmpty() -> {
                renderState(
                    MoviesState.Empty(
                        message = getApplication<Application>().getString(R.string.nothing_found),
                    )
                )
            }

            else -> {
                renderState(
                    MoviesState.Content(
                        movies = movies,
                    )
                )
            }
        }

    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

    fun toggleFavorite(movie: Movie) {
        if (movie.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movie)
        } else {
            moviesInteractor.addMovieToFavorites(movie)
        }

        updateMovieContent(movie.id, movie.copy(inFavorite = !movie.inFavorite))
    }

    private fun updateMovieContent(movieId: String, newMovie: Movie) {
        val currentState = stateLiveData.value

        if (currentState is MoviesState.Content) {
            val movieIndex = currentState.movies.indexOfFirst { it.id == movieId }

            if (movieIndex != -1) {
                stateLiveData.value = MoviesState.Content(
                    currentState.movies.toMutableList().also {
                        it[movieIndex] = newMovie
                    }
                )
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }

}