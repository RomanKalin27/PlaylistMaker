package com.practicum.playlistmaker.playlistCreator.domain.models



data class Playlist(
    val dbId: Int,
    val playlistName: String,
    val playlistDesc: String?,
    val artworkUri: String?,
    var trackList: ArrayList<Long>,
    var numberOfTracks: Int,
)