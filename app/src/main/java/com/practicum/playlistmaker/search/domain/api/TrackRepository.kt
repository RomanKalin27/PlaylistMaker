package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackRepository {
    fun saveTrack(track: Track, index: Int, trackString: String)
    fun removeTracks(index: Int)
    fun getHistoryStrings(index: Int): String?
}