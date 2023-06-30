package com.practicum.playlistmaker.player.domain.models

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerState {
    var track = Track(0, "", "", "", "", "", "", "", "", "")
    var playerState = 0
    var timePlayed = R.string.time.toString()
}