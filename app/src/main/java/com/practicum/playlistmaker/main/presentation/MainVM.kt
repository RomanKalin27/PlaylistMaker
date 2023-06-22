package com.practicum.playlistmaker.main.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.main.domain.usecases.LibraryIntentUseCase
import com.practicum.playlistmaker.main.domain.usecases.SearchIntentUseCase
import com.practicum.playlistmaker.main.domain.usecases.SettingsIntentUseCase
import com.practicum.playlistmaker.settings.domain.usecases.GetThemeUseCase

class MainVM(
    private val searchIntentUseCase: SearchIntentUseCase,
    private val libraryIntentUseCase: LibraryIntentUseCase,
    private val settingsIntentUseCase: SettingsIntentUseCase,
    private val getThemeUseCase: GetThemeUseCase,
) : ViewModel() {
    fun setTheme() {
        if (getThemeUseCase.execute()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun searchIntent() {
        searchIntentUseCase.execute()
    }

    fun libraryIntent() {
        libraryIntentUseCase.execute()
    }

    fun settingsIntent() {
        settingsIntentUseCase.execute()
    }
}