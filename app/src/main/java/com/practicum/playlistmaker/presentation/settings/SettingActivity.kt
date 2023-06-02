package com.practicum.playlistmaker.presentation.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.settings.SettingsInteractorImpl
import com.practicum.playlistmaker.data.repository.ThemeRepositoryImpl
import com.practicum.playlistmaker.domain.models.Switch
import com.practicum.playlistmaker.domain.usecase.settings.AgreementBtnUseCase
import com.practicum.playlistmaker.domain.usecase.settings.GetThemeUseCase
import com.practicum.playlistmaker.domain.usecase.settings.SaveThemeUseCase
import com.practicum.playlistmaker.domain.usecase.settings.ShareBtnUseCase
import com.practicum.playlistmaker.domain.usecase.settings.SupportBtnUseCase

class SettingActivity : AppCompatActivity() {
    private val settingsInteractor by lazy {
        SettingsInteractorImpl(
            this,
            getString(R.string.AndroidDevLink),
            getString(R.string.userAgreementLink),
            getString(R.string.recipientEmail),
            getString(R.string.subject),
            getString(R.string.message)
        )
    }
    private val shareBtnUseCase by lazy { ShareBtnUseCase(settingsInteractor) }
    private val supportBtnUseCase by lazy { SupportBtnUseCase(settingsInteractor) }
    private val agreementBtnUseCase by lazy { AgreementBtnUseCase(settingsInteractor) }
    private val themeRepository by lazy { ThemeRepositoryImpl(context = applicationContext) }
    private val saveThemeUseCase by lazy { SaveThemeUseCase(themeRepository = themeRepository) }
    private val getThemeUseCase by lazy { GetThemeUseCase(themeRepository = themeRepository) }
    private val goBackBtn by lazy { findViewById<ImageButton>(R.id.goBackBtn) }
    private val nightModeSwitch by lazy { findViewById<SwitchCompat>(R.id.NightModeSwitch) }
    private val shareBtn by lazy { findViewById<Button>(R.id.shareBtn) }
    private val supportBtn by lazy { findViewById<Button>(R.id.supportBtn) }
    private val agreementBtn by lazy { findViewById<Button>(R.id.agreementBtn) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        goBackBtn.setOnClickListener {
            finish()
        }
        if (getThemeUseCase.execute()
        ) {
            nightModeSwitch.isChecked = true
        }
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveThemeUseCase.execute(Switch(isChecked))
        }
        shareBtn.setOnClickListener {
            shareBtnUseCase.execute()
        }
        supportBtn.setOnClickListener {
            supportBtnUseCase.execute()
        }
        agreementBtn.setOnClickListener {
            agreementBtnUseCase.execute()
        }
    }
}