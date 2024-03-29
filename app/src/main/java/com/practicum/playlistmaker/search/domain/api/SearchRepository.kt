package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface SearchRepository {
    fun searchTracks(expression: String): Flow<List<Track>>
    fun checkConnection(): Boolean
}