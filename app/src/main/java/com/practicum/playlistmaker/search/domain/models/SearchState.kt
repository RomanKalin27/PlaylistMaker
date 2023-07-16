package com.practicum.playlistmaker.search.domain.models


class SearchState(
    val state: Int,
) {
    var historyList = ArrayList<Track>()
    var searchList = ArrayList<Track>()
    var searchInput = ""

    companion object {
        const val HISTORY_STATE = 1
        const val SEARCH_RESULTS = 2
        const val LOADING_STATE = 3
        const val NOTHING_FOUND = 4
        const val NO_CONNECTION = 5
    }
}