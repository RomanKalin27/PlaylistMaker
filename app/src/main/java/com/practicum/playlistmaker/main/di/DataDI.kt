package com.practicum.playlistmaker.main.di

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.app.App.Companion.SHARED_PREFS
import com.practicum.playlistmaker.player.data.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFS, Application.MODE_PRIVATE)
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            context = get(),
            sharedPrefs = get(),
            trackRepository = get(),
            mediaPlayer = get()
        )
    }
    single<TrackRepository> {
        TrackRepositoryImpl(
            sharedPrefs = get()
        )
    }
    single<ThemeRepository> {
        ThemeRepositoryImpl(sharedPrefs = get())
    }
}