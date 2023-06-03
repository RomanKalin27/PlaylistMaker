package com.practicum.playlistmaker.settings.domain.usecases

import com.practicum.playlistmaker.settings.domain.api.ThemeRepository

class GetThemeUseCase(private val themeRepository: ThemeRepository) {
    fun execute(): Boolean {
        return themeRepository.getTheme()
    }

}