package com.practicum.playlistmaker.search.data

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl.Companion.INPUT
import com.practicum.playlistmaker.search.domain.api.SearchInputRepository

class SearchInputRepositoryImpl(context: Context) : SearchInputRepository {
    private val sharedPrefs =
        context.getSharedPreferences(TrackRepositoryImpl.SHARED_PREFS, Application.MODE_PRIVATE)

    override fun saveInput(input: String) {
        sharedPrefs.edit()
            .putString(INPUT, input)
            .apply()
    }

    override fun getInput(): String? {
        return sharedPrefs.getString(INPUT, "")
    }

}