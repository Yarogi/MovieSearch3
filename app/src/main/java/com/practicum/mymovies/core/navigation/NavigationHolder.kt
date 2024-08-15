package com.practicum.mymovies.core.navigation

import androidx.fragment.app.Fragment

/**
 * Сущность для хранения ссылки на Navigator.
 */
interface NavigationHolder {

    fun attachNavigator(navigator: Navigator)
    fun dettachNavigator()
    fun openFragment(fragment: Fragment)

}