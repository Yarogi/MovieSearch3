package com.practicum.mymovies.presentation.names

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.mymovies.R
import com.practicum.mymovies.domain.api.NamesInteractor
import com.practicum.mymovies.domain.models.Person
import com.practicum.mymovies.presentation.movies.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NamesViewModel(
    private val context: Context,
    private val namesInteractor: NamesInteractor,
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<NamesState>()
    val observeState: LiveData<NamesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun searchDebounce(searchText: String) {

        if (searchText == latestSearchText) {
            return
        }
        this.latestSearchText = searchText
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(searchText)
        }


    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(NamesState.Loading)
            namesInteractor.searchNames(
                newSearchText,
                object : NamesInteractor.NamesSearchConsumer {
                    override fun consume(foundNames: List<Person>?, errorMessage: String?) {

                        val persons = mutableListOf<Person>()
                        if (foundNames != null) {
                            persons.addAll(foundNames)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    NamesState.Error(
                                        context.getString(R.string.something_went_wrong)
                                    )
                                )
                                showToast.postValue(errorMessage)
                            }

                            persons.isEmpty() -> {
                                renderState(
                                    NamesState.Empty(
                                        context.getString(R.string.nothing_found)
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    NamesState.Content(persons)
                                )
                            }

                        }

                    }

                }

            )

        }
    }

    private fun renderState(state: NamesState) {
        stateLiveData.postValue(state)
    }

}