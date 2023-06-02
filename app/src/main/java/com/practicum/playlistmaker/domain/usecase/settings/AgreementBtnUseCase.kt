package com.practicum.playlistmaker.domain.usecase.settings

class AgreementBtnUseCase(private val settingsInteractor: SettingsInteractor) {
    fun execute() {
        settingsInteractor.agreementBtn()
    }
}