package com.practicum.mymovies.presentation.movies

import com.practicum.mymovies.ui.movies.models.MovieState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoviesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: MovieState)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(additionalMessage: String)
}