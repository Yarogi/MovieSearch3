package com.practicum.mymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.practicum.mymovies.R
import com.practicum.mymovies.core.navigation.NavigatorHolder
import com.practicum.mymovies.core.navigation.impl.NavigatorImpl
import com.practicum.mymovies.databinding.ActivityRootBinding
import com.practicum.mymovies.ui.movies.MoviesFragment
import org.koin.android.ext.android.inject

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    // Заинжектили NavigatorHolder,
    // чтобы прикрепить к нему Navigator
    private val navigatorHolder: NavigatorHolder by inject()

    // Создали Navigator
    val navigator = NavigatorImpl(
        fragmentContainerViewId = R.id.rootFragmentContainerView,
        fragmentManager = supportFragmentManager
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Привязываем вёрстку к экрану
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // С помощью навигатора открываем первый экран
            navigator.openFragment(MoviesFragment())
        }

    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.attachNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.dettachNavigator()
    }

}