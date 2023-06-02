package com.practicum.playlistmaker.domain.usecase.settings

import com.practicum.playlistmaker.domain.repository.ThemeRepository

class GetThemeUseCase(private val themeRepository: ThemeRepository) {
    fun execute(): Boolean {
        return themeRepository.getTheme()
    }

}