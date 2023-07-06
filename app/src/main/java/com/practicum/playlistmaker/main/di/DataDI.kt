package com.practicum.playlistmaker.main.di

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.app.App.Companion.SHARED_PREFS
import com.practicum.playlistmaker.player.data.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackApi
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    single<TrackApi>
    {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }
    single<Gson>{
        Gson()
    }
}