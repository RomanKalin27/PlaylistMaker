package com.practicum.playlistmaker.search.domain.usecases

import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track


class SaveTrackUseCase(val trackRepository: TrackRepository) {
    private val getHistoryUseCase = GetHistoryUseCase(trackRepository)


    fun execute(track: Track) {
        val historyList = getHistoryUseCase.execute()
        if (historyList.contains((track))) {
            historyList.remove(track)
        }
        if (historyList.size >= historySize) {
            historyList.removeAt(9)
        }
        historyList.add(0, track)
        historyList.forEach {
            trackRepository.saveTrack(track, historyList.indexOf(it), createJsonFromTrack(it))
        }
    }

    private fun createJsonFromTrack(track: Track): String {
        return Gson().toJson(track)
    }

    companion object {
        const val historySize = 10
    }
}