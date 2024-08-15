package com.practicum.mymovies.core.navigation.impl

import androidx.fragment.app.Fragment
import com.practicum.mymovies.core.navigation.NavigatorHolder
import com.practicum.mymovies.core.navigation.Router

class RouterImpl : Router {

    override val navigatorHolder: NavigatorHolder = NavigatorHolderImpl()

    override fun openFragment(fragment: Fragment) {
        navigatorHolder.openFragment(fragment)
    }
}