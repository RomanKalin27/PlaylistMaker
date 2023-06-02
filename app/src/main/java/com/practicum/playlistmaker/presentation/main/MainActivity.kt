package com.practicum.playlistmaker.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.main.MenuInteractorImpl
import com.practicum.playlistmaker.data.repository.ThemeRepositoryImpl
import com.practicum.playlistmaker.domain.usecase.settings.GetThemeUseCase
import com.practicum.playlistmaker.domain.usecase.menu.LibraryIntentUseCase
import com.practicum.playlistmaker.domain.usecase.menu.SearchIntentUseCase
import com.practicum.playlistmaker.domain.usecase.menu.SettingsIntentUseCase

class MainActivity : AppCompatActivity() {
    private val menuInteractor by lazy { MenuInteractorImpl(this) }
    private val settingsIntentUseCase by lazy { SettingsIntentUseCase(menuInteractor) }
    private val searchIntentUseCase by lazy { SearchIntentUseCase(menuInteractor) }
    private val libraryIntentUseCase by lazy { LibraryIntentUseCase(menuInteractor) }
    private val themeRepository by lazy { ThemeRepositoryImpl(context = applicationContext) }
    private val getThemeUseCase by lazy { GetThemeUseCase(themeRepository = themeRepository) }
    private val searchButton by lazy { findViewById<Button>(R.id.search_button) }
    private val libraryButton by lazy { findViewById<Button>(R.id.library_button) }
    private val settingsButton by lazy { findViewById<Button>(R.id.settings_button) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (getThemeUseCase.execute()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        searchButton.setOnClickListener {
            searchIntentUseCase.execute()
        }

        libraryButton.setOnClickListener {
            libraryIntentUseCase.execute()
        }

        settingsButton.setOnClickListener {
            settingsIntentUseCase.execute()
        }
    }
}