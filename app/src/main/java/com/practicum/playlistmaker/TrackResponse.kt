package com.practicum.playlistmaker

import com.practicum.playlistmaker.domain.models.Track


data class TrackResponse (
    val searchType: String,
    val expression: String,
    val results: List<Track>,
)