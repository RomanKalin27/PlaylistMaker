package com.practicum.playlistmaker.search.domain.models

import com.practicum.playlistmaker.search.ui.TrackAdapter

class SearchState(
    val state: Int,
    val trackAdapter: TrackAdapter,
) {
    var searchInput = ""

    companion object {
        const val HISTORY_STATE = 1
        const val SEARCH_RESULTS = 2
        const val LOADING_STATE = 3
        const val NOTHING_FOUND = 4
        const val NO_CONNECTION = 5
        const val BLANK_STATE = 6
    }
}