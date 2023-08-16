package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(id: Long)
}