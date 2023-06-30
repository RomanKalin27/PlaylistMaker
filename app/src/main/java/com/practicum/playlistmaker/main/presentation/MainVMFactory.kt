package com.practicum.playlistmaker.main.presentation


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.main.data.MenuInteractorImpl
import com.practicum.playlistmaker.main.domain.usecases.LibraryIntentUseCase
import com.practicum.playlistmaker.main.domain.usecases.SearchIntentUseCase
import com.practicum.playlistmaker.main.domain.usecases.SettingsIntentUseCase
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.usecases.GetThemeUseCase

class MainVMFactory(context: Context) : ViewModelProvider.Factory {
    private val menuInteractor by lazy { MenuInteractorImpl(context) }
    private val settingsIntentUseCase by lazy { SettingsIntentUseCase(menuInteractor) }
    private val searchIntentUseCase by lazy { SearchIntentUseCase(menuInteractor) }
    private val libraryIntentUseCase by lazy { LibraryIntentUseCase(menuInteractor) }
    private val themeRepository by lazy { ThemeRepositoryImpl(context = context) }
    private val getThemeUseCase by lazy { GetThemeUseCase(themeRepository = themeRepository) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainVM(
            searchIntentUseCase = searchIntentUseCase,
            libraryIntentUseCase = libraryIntentUseCase,
            settingsIntentUseCase = settingsIntentUseCase,
            getThemeUseCase = getThemeUseCase,
        ) as T
    }

}