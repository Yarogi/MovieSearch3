package com.practicum.mymovies.ui.movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.mymovies.util.MoviesApplication
import com.practicum.mymovies.util.Creator
import com.practicum.mymovies.ui.poster.PosterActivity
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.models.Movie
import com.practicum.mymovies.presentation.movies.MoviesSearchPresenter
import com.practicum.mymovies.presentation.movies.MoviesView
import com.practicum.mymovies.ui.movies.models.MovieState

class MoviesActivity : Activity(), MoviesView {


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
//        Шаг 1.3. Сохранить Presenter в статическом состоянии
//        private var moviesSearchPresenter: MoviesSearchPresenter? = null
    }

    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private var textWatcher: TextWatcher? = null

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var moviesSearchPresenter: MoviesSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

//        Шаг 1.2. Callback onRetainNonConfigurationInstance
//        moviesSearchPresenter = lastNonConfigurationInstance as? MoviesSearchPresenter
//        Шаг 1.4. Сохранить Presenter в Application
        moviesSearchPresenter =
            (this.applicationContext as? MoviesApplication)?.moviesSearchPresenter

        if (moviesSearchPresenter == null) {
            moviesSearchPresenter = Creator.provideMoviesSearchPresenter(
                context = this.applicationContext
            )
//            Шаг 1.4. Сохранить Presenter в Application
            (this.applicationContext as? MoviesApplication)?.moviesSearchPresenter =
                moviesSearchPresenter
        }

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.locations)
        progressBar = findViewById(R.id.progressBar)

        moviesList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                moviesSearchPresenter?.searchDebounce(
                    changedText = p0?.toString() ?: ""
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }
    }

    override fun onStart() {
        super.onStart()
        moviesSearchPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        moviesSearchPresenter?.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        moviesSearchPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        moviesSearchPresenter?.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesSearchPresenter?.detachView()
    }

    override fun onDestroy() {
        super.onDestroy()

        textWatcher?.let { queryInput.removeTextChangedListener(it) }

        //    Шаг 2.1. Правильно привязать View к Presenter
        moviesSearchPresenter?.detachView()

        moviesSearchPresenter?.onDestroy()

        if (isFinishing()) {
            (this.applicationContext as? MoviesApplication)?.moviesSearchPresenter = null
        }
    }

//    Шаг 1.2. Callback onRetainNonConfigurationInstance
//    override fun onRetainNonConfigurationInstance(): Any? {
//        return moviesSearchPresenter
//    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    override fun render(state: MovieState) {
//        when {
//            state.isLoading -> showLoading()
//            state.errorMessage != null -> showError(state.errorMessage)
//            else -> showContent(state.movies)
//        }
        when (state) {
            is MovieState.Loading -> showLoading()
            is MovieState.Content -> showContent(state.movies)
            is MovieState.Error -> showError(state.errorMessage)
            is MovieState.Empty -> showEmpty(state.message)
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}