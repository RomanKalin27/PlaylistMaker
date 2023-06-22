package com.practicum.playlistmaker.settings.domain.usecases

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor

class AgreementBtnUseCase(private val settingsInteractor: SettingsInteractor) {
    fun execute() {
        settingsInteractor.agreementBtn()
    }
}