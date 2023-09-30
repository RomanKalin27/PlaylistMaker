package com.practicum.playlistmaker.library.presentation.favorites

import com.practicum.playlistmaker.search.domain.models.Track

sealed interface FavoritesState {
    object Loading : FavoritesState

    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

    class Empty(
    ) : FavoritesState
}