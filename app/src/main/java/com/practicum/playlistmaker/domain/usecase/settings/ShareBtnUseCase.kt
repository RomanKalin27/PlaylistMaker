package com.practicum.playlistmaker.domain.usecase.settings



class ShareBtnUseCase(private val settingsInteractor: SettingsInteractor) {
    fun execute() {
        settingsInteractor.shareBtn()
    }
}