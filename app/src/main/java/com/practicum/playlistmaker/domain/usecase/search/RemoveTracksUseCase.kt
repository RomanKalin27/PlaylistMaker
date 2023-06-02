package com.practicum.playlistmaker.domain.usecase.search

import com.practicum.playlistmaker.domain.repository.TrackRepository

class RemoveTracksUseCase(val trackRepository: TrackRepository) {
    private val intArray = ArrayList<Int>()
    fun execute() {
        intArray.addAll(0..9)
        intArray.forEach() {
            trackRepository.removeTracks(it)
        }

    }
}