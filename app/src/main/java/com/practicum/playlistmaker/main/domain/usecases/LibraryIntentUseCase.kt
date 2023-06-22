package com.practicum.playlistmaker.main.domain.usecases

import com.practicum.playlistmaker.main.domain.api.MenuInteractor

class LibraryIntentUseCase(private val menuInteractor: MenuInteractor) {
    fun execute() {
        menuInteractor.libraryIntent()
    }
}