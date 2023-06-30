package com.practicum.playlistmaker.settings.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.settings.data.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.settings.domain.usecases.AgreementBtnUseCase
import com.practicum.playlistmaker.settings.domain.usecases.GetThemeUseCase
import com.practicum.playlistmaker.settings.domain.usecases.SaveThemeUseCase
import com.practicum.playlistmaker.settings.domain.usecases.ShareBtnUseCase
import com.practicum.playlistmaker.settings.domain.usecases.SupportBtnUseCase

class SettingVMFactory(context: Context) : ViewModelProvider.Factory {
    private val settingsInteractor by lazy {
        SettingsInteractorImpl(
            context,
            "https://practicum.yandex.ru/android-developer/",
            "https://yandex.ru/legal/practicum_offer/",
            "k4linromain@yandex.ru",
            "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            "Спасибо разработчикам и разработчицам за крутое приложение!",
        )
    }
    private val shareBtnUseCase by lazy { ShareBtnUseCase(settingsInteractor) }
    private val supportBtnUseCase by lazy { SupportBtnUseCase(settingsInteractor) }
    private val agreementBtnUseCase by lazy { AgreementBtnUseCase(settingsInteractor) }
    private val themeRepository by lazy { ThemeRepositoryImpl(context = context) }
    private val saveThemeUseCase by lazy { SaveThemeUseCase(themeRepository = themeRepository) }
    private val getThemeUseCase by lazy { GetThemeUseCase(themeRepository = themeRepository) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingVM(
            shareBtnUseCase = shareBtnUseCase,
            supportBtnUseCase = supportBtnUseCase,
            agreementBtnUseCase = agreementBtnUseCase,
            saveThemeUseCase = saveThemeUseCase,
            getThemeUseCase = getThemeUseCase,
        ) as T
    }
}
