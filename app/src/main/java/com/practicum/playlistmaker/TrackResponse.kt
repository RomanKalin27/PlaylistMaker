package com.practicum.playlistmaker


import java.util.*

data class TrackResponse (
    val searchType: String,
    val expression: String,
    val results: List<Track>,
)