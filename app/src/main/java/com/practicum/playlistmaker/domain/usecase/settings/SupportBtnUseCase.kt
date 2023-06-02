package com.practicum.playlistmaker.domain.usecase.settings


class SupportBtnUseCase(private val settingsInteractor: SettingsInteractor) {
    fun execute() {
        settingsInteractor.supportBtn()
    }
}