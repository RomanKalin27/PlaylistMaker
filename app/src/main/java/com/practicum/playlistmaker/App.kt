package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.gson.Gson

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = applicationContext.getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
        historyList = ArrayList<Track>()
        sharedPreferences.getStringSet(SearchActivity.NEW_TRACK, null)?.forEach {
            historyList.add(0, createTrackFromJson(it))
        }
        if (sharedPreferences.getBoolean("MODE", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

    }

    companion object {
        lateinit var sharedPreferences: SharedPreferences
            private set
        lateinit var historyList: ArrayList<Track>
            private set
        const val SEARCH_HISTORY = "SEARCH_HISTORY"

    }

    private fun createTrackFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}