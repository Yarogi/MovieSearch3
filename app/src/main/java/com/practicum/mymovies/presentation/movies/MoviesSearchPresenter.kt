package com.practicum.mymovies.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.practicum.mymovies.util.Creator
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.api.MoviesInteractor
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.ui.movies.models.MovieState
import moxy.MvpPresenter

class MoviesSearchPresenter(
    private val context: Context
) : MvpPresenter<MoviesView>() {

    private var latestSearchText: String? = null

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val moviesInteractor = Creator.provideMoviesInteractor(context)

    private val movies = ArrayList<Movie>()

    private val handler = Handler(Looper.getMainLooper())

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(
                MovieState.Loading
            )

            moviesInteractor.searchMovies(
                newSearchText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            if (foundMovies != null) {
                                movies.clear()
                                movies.addAll(foundMovies)
                            }

                            when {
                                errorMessage != null -> {
                                    renderState(
                                        MovieState.Error(
                                            errorMessage = context.getString(R.string.something_went_wrong)
                                        )
                                    )
                                    viewState.showToast(errorMessage)
                                }

                                movies.isEmpty() -> {
                                    renderState(
                                        MovieState.Empty(
                                            message = context.getString(R.string.nothing_found)
                                        )
                                    )

                                }

                                else -> {
                                    renderState(
                                        MovieState.Content(
                                            movies = movies
                                        )
                                    )
                                }
                            }

                        }
                    }
                }
            )
        }
    }

    private fun renderState(state: MovieState) {
        viewState.render(state)
    }

}