package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchRepository {
    fun searchTracks(expression: String): List<Track>
    fun checkConnection(): Boolean
}