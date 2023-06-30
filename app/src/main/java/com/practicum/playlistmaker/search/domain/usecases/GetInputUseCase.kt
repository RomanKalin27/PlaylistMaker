package com.practicum.playlistmaker.search.domain.usecases

import com.practicum.playlistmaker.search.domain.api.SearchInputRepository

class GetInputUseCase(private val searchInputRepository: SearchInputRepository) {
    fun execute(): String{
       return searchInputRepository.getInput() ?: ""
    }
}