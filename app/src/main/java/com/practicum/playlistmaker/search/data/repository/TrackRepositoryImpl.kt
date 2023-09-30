package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.TrackRepository

class TrackRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : TrackRepository {
    override fun saveTrack(track: Track, index: Int, trackString: String) {
        sharedPrefs.edit()
            .putString(index.toString(), trackString)
            .apply()
    }

    override fun removeTracks(index: Int) {
        sharedPrefs.edit()
            .remove(index.toString())
            .apply()
    }

    override fun getHistoryStrings(index: Int): String? {
        return sharedPrefs.getString(index.toString(), null)

    }
}