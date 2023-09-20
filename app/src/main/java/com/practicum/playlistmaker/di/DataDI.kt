package com.practicum.playlistmaker.di

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.app.App.Companion.SHARED_PREFS
import com.practicum.playlistmaker.library.data.TrackDbConvertor
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.data.repository.FavoritesRepositoryImpl
import com.practicum.playlistmaker.library.domain.db.FavoritesRepository
import com.practicum.playlistmaker.playlistCreator.data.db.PlaylistDbConvertor
import com.practicum.playlistmaker.playlistCreator.data.repository.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.TrackSearcher
import com.practicum.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.network.TrackApi
import com.practicum.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchRepository
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
    single<TrackRepository> {
        TrackRepositoryImpl(
            sharedPrefs = get()
        )
    }
    single<ThemeRepository> {
        ThemeRepositoryImpl(sharedPrefs = get())
    }
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }
    single<NetworkClient> {
        TrackSearcher(androidContext(), itunesService = get())
    }
    single<TrackApi> {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }
    single {
        Gson()
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(appDatabase = get(), trackDbConvertor = get())
    }
    factory { TrackDbConvertor() }
    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(
            appDatabase = get(),
            playlistDbConvertor = get(),
            gson = get(),
            trackDbConvertor = get(),
            context = androidContext(),
        )
    }
    factory { PlaylistDbConvertor(Gson()) }
}