package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
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
    factory<SearchInteractor>{
        SearchInteractorImpl(repository = get())
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
    factory {
        ShareBtnUseCase(settingsInteractor = get())
    }
    factory {
        SupportBtnUseCase(settingsInteractor = get())
    }
    factory {
        AgreementBtnUseCase(settingsInteractor = get())
    }
    factory {
        GetThemeUseCase(themeRepository = get())
    }
    factory {
        SaveThemeUseCase(themeRepository = get())
    }
    factory {
        SaveTrackUseCase(trackRepository = get(), gson = get())
    }
    factory {
        GetHistoryUseCase(trackRepository = get())
    }
    factory {
        RemoveTracksUseCase(trackRepository = get())
    }
    factory {
        MediaPlayer()
    }
}