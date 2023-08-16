package com.practicum.playlistmaker.player.domain.models

import com.practicum.playlistmaker.search.domain.models.Track

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val isPaused: Boolean,
    val progress: String,
) {
    var track = Track(0, "", "", "", "", "", "", "", "", "")

    class Default : PlayerState(false, true, "00:00")

    class Prepared : PlayerState(true, true, "00:00")

    class Playing(progress: String) : PlayerState(true, false, progress)

    class Paused(progress: String) : PlayerState(true, true, progress)
}