package com.practicum.playlistmaker.main.di

import com.practicum.playlistmaker.main.presentation.MainViewModel
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel>() {
        MainViewModel(
            searchIntentUseCase = get(),
            settingsIntentUseCase = get(),
            libraryIntentUseCase = get(),
            getThemeUseCase = get(),
        )
    }
    viewModel<SearchViewModel>() {
        SearchViewModel(
            getHistoryUseCase = get(),
            removeTracksUseCase = get(),
        )
    }
    viewModel<PlayerViewModel>() {
        PlayerViewModel(
            playerInteractor = get(),
            trackRepository = get(),
        )
    }
    viewModel<SettingViewModel>() {
        SettingViewModel(
            agreementBtnUseCase = get(),
            getThemeUseCase = get(),
            saveThemeUseCase = get(),
            shareBtnUseCase = get(),
            supportBtnUseCase = get(),
        )
    }
}
