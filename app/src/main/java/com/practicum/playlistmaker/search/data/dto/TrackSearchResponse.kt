package com.practicum.playlistmaker.search.data.dto


class TrackSearchResponse(
    val searchType: String,
    val expression: String,
    val results: List<TrackDto>,
) : Response()