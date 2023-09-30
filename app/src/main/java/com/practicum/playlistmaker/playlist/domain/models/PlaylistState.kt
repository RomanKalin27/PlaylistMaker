package com.practicum.playlistmaker.playlist.domain.models

import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

class PlaylistState(
    var playlistId: Int,
    var playlistName: String,
    var playlistDesc: String,
    var artworkPath: String,
    var playlistLength: Int,
    var numberOfTracks: Int,
    var trackList: ArrayList<Track>,
    var navigateUp: Boolean,
)