package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun searchTracks(expression: String): Flow<List<Track>> {
        return repository.searchTracks(expression)
    }

    override fun isOnline(): Boolean {
        return repository.checkConnection()
    }

}