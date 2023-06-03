package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.models.Track


data class TrackResponse (
    val searchType: String,
    val expression: String,
    val results: List<Track>,
)