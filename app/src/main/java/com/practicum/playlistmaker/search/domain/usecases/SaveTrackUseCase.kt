package com.practicum.playlistmaker.search.domain.usecases

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track


class SaveTrackUseCase(
    val trackRepository: TrackRepository,
    private val gson: Gson,
    private val context: Context
) {
    fun execute(track: Track, historyList: ArrayList<Track>) {
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
        val trackIntent = Intent(context, PlayerActivity::class.java)
        trackIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(trackIntent)
    }

    private fun createJsonFromTrack(track: Track): String {
        return gson.toJson(track)
    }

    companion object {
        const val historySize = 10
    }
}