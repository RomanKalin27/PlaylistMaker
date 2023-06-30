package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.models.Switch
import com.practicum.playlistmaker.settings.domain.usecases.AgreementBtnUseCase
import com.practicum.playlistmaker.settings.domain.usecases.GetThemeUseCase
import com.practicum.playlistmaker.settings.domain.usecases.SaveThemeUseCase
import com.practicum.playlistmaker.settings.domain.usecases.ShareBtnUseCase
import com.practicum.playlistmaker.settings.domain.usecases.SupportBtnUseCase

class SettingViewModel(
    private val shareBtnUseCase: ShareBtnUseCase,
    private val supportBtnUseCase: SupportBtnUseCase,
    private val agreementBtnUseCase: AgreementBtnUseCase,
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getThemeUseCase: GetThemeUseCase,
) : ViewModel() {
    private val resultSwitch = MutableLiveData<Boolean>()
    fun getSwitchResult(): LiveData<Boolean> {
        resultSwitch.value = getThemeUseCase.execute()
        return resultSwitch
    }

    fun saveTheme(isChecked: Boolean) {
        saveThemeUseCase.execute(Switch(isChecked))
    }

    fun shareBtn() {
        shareBtnUseCase.execute()
    }

    fun supportBtn() {
        supportBtnUseCase.execute()
    }

    fun agreementBtn() {
        agreementBtnUseCase.execute()
    }
}