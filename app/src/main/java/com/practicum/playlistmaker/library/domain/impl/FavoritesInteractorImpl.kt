package com.practicum.playlistmaker.library.domain.impl


import com.practicum.playlistmaker.library.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.library.domain.db.FavoritesRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.favoriteTracks()
    }

    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun deleteTrack(id: Long) {
        favoritesRepository.deleteTrack(id)
    }
}