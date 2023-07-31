package com.practicum.playlistmaker.root.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.usecases.GetThemeUseCase

class RootViewModel(
    private val getThemeUseCase: GetThemeUseCase,
) : ViewModel() {
    fun setTheme() {
        if (getThemeUseCase.execute()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}