package com.practicum.playlistmaker.domain.usecase.search

import com.google.gson.Gson
import com.practicum.playlistmaker.domain.repository.TrackRepository
import com.practicum.playlistmaker.domain.models.Track


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