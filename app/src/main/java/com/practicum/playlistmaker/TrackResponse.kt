package com.practicum.playlistmaker


import java.util.*

class TrackResponse (val searchType: String,
    val expression: String,
    val results: List<Track>,
)