package com.practicum.playlistmaker.search.data

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : TrackRepository {
    override fun saveTrack(track: Track, index: Int, trackString: String) {
        val trackTime =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        sharedPrefs.edit()
            .putString(TRACK_NAME, track.trackName)
            .putString(ARTIST_NAME, track.artistName)
            .putString(ARTWORK, track.artworkUrl100)
            .putString(TRACK_TIME, trackTime.toString())
            .putString(COLLECTION_NAME, track.collectionName)
            .putString(RELEASE_DATE, track.releaseDate.substring(0, 4))
            .putString(PRIMARY_GENRE_NAME, track.primaryGenreName)
            .putString(COUNTRY, track.country)
            .putString(PREVIEW_URL, track.previewUrl)
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

    override fun getTrackName(): String? {
        return sharedPrefs.getString(TRACK_NAME, null)
    }

    override fun getArtistName(): String? {
        return sharedPrefs.getString(ARTIST_NAME, null)
    }

    override fun getArtwork(): String? {
        return sharedPrefs.getString(ARTWORK, null)
    }

    override fun getTrackTime(): String? {
        return sharedPrefs.getString(TRACK_TIME, null)
    }

    override fun getCollectionName(): String? {
        return sharedPrefs.getString(COLLECTION_NAME, null)
    }

    override fun getReleaseDate(): String? {
        return sharedPrefs.getString(RELEASE_DATE, null)
    }

    override fun getPrimaryGenreName(): String? {
        return sharedPrefs.getString(PRIMARY_GENRE_NAME, null)
    }

    override fun getCountry(): String? {
        return sharedPrefs.getString(COUNTRY, null)
    }

    override fun getPreviewUrl(): String? {
        return sharedPrefs.getString(PREVIEW_URL, null)
    }


    companion object {
        const val TRACK_NAME = "TRACK_NAME"
        const val ARTIST_NAME = "ARTIST_NAME"
        const val ARTWORK = "ARTWORK"
        const val TRACK_TIME = "TRACK_TIME"
        const val COLLECTION_NAME = "COLLECTION_NAME"
        const val RELEASE_DATE = "RELEASE_DATE"
        const val PRIMARY_GENRE_NAME = "PRIMARY_GENRE_NAME"
        const val COUNTRY = "COUNTRY"
        const val PREVIEW_URL = "PREVIEW_URL"
    }
}