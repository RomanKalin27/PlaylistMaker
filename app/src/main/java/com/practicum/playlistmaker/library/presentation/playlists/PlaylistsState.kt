package com.practicum.playlistmaker.library.presentation.playlists

import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist

sealed interface PlaylistsState {

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    class Empty() : PlaylistsState
}