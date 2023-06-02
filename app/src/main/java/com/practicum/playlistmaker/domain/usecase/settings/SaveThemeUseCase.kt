package com.practicum.playlistmaker.domain.usecase.settings

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.models.Switch
import com.practicum.playlistmaker.domain.repository.ThemeRepository

class SaveThemeUseCase(private val themeRepository: ThemeRepository) {
    fun execute(state: Switch) {
        themeRepository.saveTheme(state)
        if (state.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}