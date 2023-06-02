package com.practicum.playlistmaker.domain.usecase.menu


class SettingsIntentUseCase(private val menuInteractor: MenuInteractor) {
    fun execute() {
        menuInteractor.settingsIntent()
    }
}