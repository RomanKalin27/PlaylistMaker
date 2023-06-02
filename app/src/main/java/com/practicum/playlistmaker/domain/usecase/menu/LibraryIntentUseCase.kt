package com.practicum.playlistmaker.domain.usecase.menu

class LibraryIntentUseCase(private val menuInteractor: MenuInteractor) {
    fun execute() {
        menuInteractor.libraryIntent()
    }
}