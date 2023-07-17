package com.practicum.playlistmaker.search.domain.usecases

import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.TrackRepository

class GetHistoryUseCase(val trackRepository: TrackRepository) {
    private val savedHistoryList = ArrayList<Track>()
    private val trackNumbers = ArrayList<Int>()
    fun execute(): ArrayList<Track> {
        trackNumbers.addAll(0..9)
        trackNumbers.forEach() {
            if ((trackRepository.getHistoryStrings(it)) !== null)
                savedHistoryList.add(it, createTrackFromJson(trackRepository.getHistoryStrings(it)))
        }
        return savedHistoryList
    }

    private fun createTrackFromJson(json: String?): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}