package com.practicum.mymovies.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.practicum.mymovies.databinding.ActivityMainBinding
import com.practicum.mymovies.ui.movies.MoviesFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        supportFragmentManager.commit {
            add(binding.fragmentContainerView.id, MoviesFragment.newInstance())
        }

    }
}