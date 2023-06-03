package com.practicum.playlistmaker.search.domain.usecases

import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.api.TrackRepository

class GetHistoryUseCase(val trackRepository: TrackRepository) {
    private val savedhistoryList = ArrayList<Track>()
    private val intArray = ArrayList<Int>()
    fun execute(): ArrayList<Track> {
        intArray.addAll(0..9)
        intArray.forEach() {
            if ((trackRepository.getHistoryStrings(it)) !== null)
                savedhistoryList.add(it, createTrackFromJson(trackRepository.getHistoryStrings(it)))
        }
        return savedhistoryList
    }

    private fun createTrackFromJson(json: String?): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}