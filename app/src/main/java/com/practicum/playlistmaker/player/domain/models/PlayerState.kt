package com.practicum.playlistmaker.player.domain.models

import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist


sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val isPaused: Boolean,
    val progress: String,
    val playlists: List<Playlist>,
) {

    class Default(playlists: List<Playlist>) : PlayerState(false, true, "00:00", playlists)

    class Prepared(playlists: List<Playlist>) : PlayerState(true, true, "00:00", playlists)

    class Playing(progress: String, playlists: List<Playlist>) :
        PlayerState(true, false, progress, playlists)

    class Paused(progress: String, playlists: List<Playlist>) :
        PlayerState(true, true, progress, playlists)
}