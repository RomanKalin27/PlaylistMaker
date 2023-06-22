package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.TrackRepository

class RemoveTracksUseCase(val trackRepository: TrackRepository) {
    private val intArray = ArrayList<Int>()
    fun execute() {
        intArray.addAll(0..9)
        intArray.forEach() {
            trackRepository.removeTracks(it)
        }

    }
}