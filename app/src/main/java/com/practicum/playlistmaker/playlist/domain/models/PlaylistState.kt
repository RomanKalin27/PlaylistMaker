package com.practicum.playlistmaker.playlist.domain.models

import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistState(
    var playlistName: String,
    var artworkPath: String,
    var playlistLength: Int,
    var numberOfTracks: Int,
    var trackList: ArrayList<Track>,
) {
}