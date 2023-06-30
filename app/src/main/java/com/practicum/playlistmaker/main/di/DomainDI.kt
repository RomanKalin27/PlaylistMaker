package com.practicum.playlistmaker.main.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.data.MenuInteractorImpl
import com.practicum.playlistmaker.main.domain.api.MenuInteractor
import com.practicum.playlistmaker.main.domain.usecases.LibraryIntentUseCase
import com.practicum.playlistmaker.main.domain.usecases.SearchIntentUseCase
import com.practicum.playlistmaker.main.domain.usecases.SettingsIntentUseCase
import com.practicum.playlistmaker.search.domain.usecases.GetHistoryUseCase
import com.practicum.playlistmaker.search.domain.usecases.RemoveTracksUseCase
import com.practicum.playlistmaker.search.domain.usecases.SaveTrackUseCase
import com.practicum.playlistmaker.settings.data.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.usecases.AgreementBtnUseCase
import com.practicum.playlistmaker.settings.domain.usecases.GetThemeUseCase
import com.practicum.playlistmaker.settings.domain.usecases.SaveThemeUseCase
import com.practicum.playlistmaker.settings.domain.usecases.ShareBtnUseCase
import com.practicum.playlistmaker.settings.domain.usecases.SupportBtnUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val domainModule = module {
    single<MenuInteractor> {
        MenuInteractorImpl(context = get())
    }
    factory<SettingsIntentUseCase> {
        SettingsIntentUseCase(menuInteractor = get())
    }
    factory<SearchIntentUseCase> {
        SearchIntentUseCase(menuInteractor = get())
    }
    factory<LibraryIntentUseCase> {
        LibraryIntentUseCase(menuInteractor = get())
    }
    single<SettingsInteractor> {
        SettingsInteractorImpl(
            context = get(),
            androidContext().getString(R.string.androidDevLink),
            androidContext().getString(R.string.userAgreementLink),
            androidContext().getString(R.string.recipientEmail),
            androidContext().getString(R.string.subject),
            androidContext().getString(R.string.message),
        )
    }
    factory<ShareBtnUseCase> {
        ShareBtnUseCase(settingsInteractor = get())
    }
    factory<SupportBtnUseCase> {
        SupportBtnUseCase(settingsInteractor = get())
    }
    factory<AgreementBtnUseCase> {
        AgreementBtnUseCase(settingsInteractor = get())
    }
    factory<GetThemeUseCase> {
        GetThemeUseCase(themeRepository = get())
    }
    factory<SaveThemeUseCase> {
        SaveThemeUseCase(themeRepository = get())
    }
    factory<SaveTrackUseCase> {
        SaveTrackUseCase(trackRepository = get())
    }
    factory<GetHistoryUseCase> {
        GetHistoryUseCase(trackRepository = get())
    }
    factory<RemoveTracksUseCase> {
        RemoveTracksUseCase(trackRepository = get())
    }
    factory<MediaPlayer> {
        MediaPlayer()
    }

}