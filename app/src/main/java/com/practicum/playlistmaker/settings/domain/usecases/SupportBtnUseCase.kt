package com.practicum.playlistmaker.settings.domain.usecases

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor


class SupportBtnUseCase(private val settingsInteractor: SettingsInteractor) {
    fun execute() {
        settingsInteractor.supportBtn()
    }
}