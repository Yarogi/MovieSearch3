package com.practicum.mymovies.core.navigation.impl

import androidx.fragment.app.Fragment
import com.practicum.mymovies.core.navigation.NavigatorHolder
import com.practicum.mymovies.core.navigation.Router

class RouterImpl(override val navigationHolder: NavigatorHolder) : Router {
    override fun openFragment(fragment: Fragment) {
        navigationHolder.openFragment(fragment)
    }
}