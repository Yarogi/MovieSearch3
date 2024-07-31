package com.practicum.mymovies.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.mymovies.data.SearchHistoryStorage

class SharedPreferencesSearchHistoryStorage(
    private val prefs: SharedPreferences,
    private val gson: Gson,
) : SearchHistoryStorage