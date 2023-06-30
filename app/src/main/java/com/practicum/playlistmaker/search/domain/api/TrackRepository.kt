package com.practicum.playlistmaker.search.domain.api

import android.widget.ImageView
import com.practicum.playlistmaker.search.domain.models.Track

interface TrackRepository {
    fun saveTrack(track: Track, index: Int, trackString: String)
    fun removeTracks(index: Int)
    fun getHistoryStrings(index: Int): String?
    fun getTrackName(): String?
    fun getArtistName(): String?
    fun getArtwork(): String?
    fun getTrackTime(): String?
    fun getCollectionName(): String?
    fun getPrimaryGenreName(): String?
    fun getReleaseDate(): String?
    fun getCountry(): String?
    fun getPreviewUrl(): String?
}