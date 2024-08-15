package com.practicum.mymovies.core.navigation

import androidx.fragment.app.Fragment

/**
 * Router — это входная точка для фрагментов,
 * которые хотят открыть следующий экран.
 */

interface Router {

    val navigationHolder: NavigatorHolder

    fun openFragment(fragment:Fragment)

}