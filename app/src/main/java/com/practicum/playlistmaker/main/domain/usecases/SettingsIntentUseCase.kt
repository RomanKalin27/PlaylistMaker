package com.practicum.playlistmaker.main.domain.usecases

import com.practicum.playlistmaker.main.domain.api.MenuInteractor


class SettingsIntentUseCase(private val menuInteractor: MenuInteractor) {
    fun execute() {
        menuInteractor.settingsIntent()
    }
}