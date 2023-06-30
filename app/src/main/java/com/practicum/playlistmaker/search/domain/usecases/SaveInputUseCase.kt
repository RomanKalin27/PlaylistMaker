package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.SearchInputRepository

class SaveInputUseCase(private val searchInputRepository: SearchInputRepository) {
    fun execute(input: String){
        searchInputRepository.saveInput(input)
    }
}