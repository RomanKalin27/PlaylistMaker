package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.presentation.FavoritesFragmentViewModel
import com.practicum.playlistmaker.library.presentation.LibraryViewModel
import com.practicum.playlistmaker.library.presentation.PlaylistsFragmentViewModel
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.root.presentation.RootViewModel
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        RootViewModel(
            getThemeUseCase = get(),
        )
    }
    viewModel {
        SearchViewModel(
            getHistoryUseCase = get(),
            saveTrackUseCase = get(),
            removeTracksUseCase = get(),
            searchInteractor = get(),
        )
    }
    viewModel {
        PlayerViewModel(
            favoritesInteractor = get(),
            appDatabase = get(),
        )
    }
    viewModel {
        SettingViewModel(
            agreementBtnUseCase = get(),
            getThemeUseCase = get(),
            saveThemeUseCase = get(),
            shareBtnUseCase = get(),
            supportBtnUseCase = get(),
        )
    }
    viewModel {
        LibraryViewModel()
    }
    viewModel {
        FavoritesFragmentViewModel(get(),)
    }
    viewModel {
        PlaylistsFragmentViewModel()
    }
}
