package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.TrackRepository

class RemoveTracksUseCase(val trackRepository: TrackRepository) {
    private val trackNumbers = ArrayList<Int>()
    fun execute() {
        trackNumbers.addAll(0..9)
        trackNumbers.forEach() {
            trackRepository.removeTracks(it)
        }

    }
}