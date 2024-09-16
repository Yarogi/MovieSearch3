package com.practicum.mymovies.presentation.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.api.HistoryInteractor
import com.practicum.mymovies.domain.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val context: Context,
    private val historyInteractor: HistoryInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<HistoryState>()
    fun observeState(): LiveData<HistoryState> = stateLiveData

    fun fillData() {
        viewModelScope.launch {
            historyInteractor.historyMovies()
                .flowOn(Dispatchers.IO)
                .collect {
                    processedResult(it)
                }
        }
    }

    fun processedResult(movies: List<Movie>) {
        if (movies.isEmpty()) {
            renderState(HistoryState.Empty(context.getString(R.string.nothing_found)))
        } else {
            renderState(HistoryState.Content(movies))
        }
    }

    fun renderState(state: HistoryState) {
        stateLiveData.postValue(state)
    }

}