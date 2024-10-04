package com.practicum.mymovies.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.mymovies.R
import com.practicum.mymovies.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.rootFragmentContainerView
            ) as NavHostFragment
        val navControler = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navControler)

        navControler.addOnDestinationChangedListener { _, destination, _ ->

            binding.bottomNavigationView.isVisible =
                destination.id == R.id.moviesFragment
                        || destination.id == R.id.namesFragment
                        || destination.id == R.id.infoFragment
                        || destination.id == R.id.historyFragment
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {

                exitConfirmDialog().show()

            }
        })

    }

    fun animateBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun exitConfirmDialog():MaterialAlertDialogBuilder{
        return MaterialAlertDialogBuilder(this)
            .setTitle("Вы действительно хотите выйти из приложения?")
            .setPositiveButton("Да"){dialog, which -> this.finish()}
            .setNegativeButton("Нет"){dialog, which -> }

    }

}