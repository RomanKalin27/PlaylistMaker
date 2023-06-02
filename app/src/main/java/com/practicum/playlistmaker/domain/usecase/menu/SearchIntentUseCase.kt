package com.practicum.playlistmaker.domain.usecase.menu

class SearchIntentUseCase(private val menuInteractor: MenuInteractor) {
    fun execute() {
        menuInteractor.searchIntent()
    }
}