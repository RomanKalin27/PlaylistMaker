package com.practicum.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.presentation.SettingVM
import com.practicum.playlistmaker.settings.presentation.SettingVMFactory

class SettingActivity : AppCompatActivity() {
    private val goBackBtn by lazy { findViewById<ImageButton>(R.id.goBackBtn) }
    private val nightModeSwitch by lazy { findViewById<SwitchCompat>(R.id.NightModeSwitch) }
    private val shareBtn by lazy { findViewById<Button>(R.id.shareBtn) }
    private val supportBtn by lazy { findViewById<Button>(R.id.supportBtn) }
    private val agreementBtn by lazy { findViewById<Button>(R.id.agreementBtn) }
    private val vm by lazy {
        ViewModelProvider(
            this,
            SettingVMFactory(this)
        )[SettingVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        vm.getSwitchResult().observe(this) {
            nightModeSwitch.isChecked = it
        }
        goBackBtn.setOnClickListener {
            finish()
        }
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            vm.saveTheme(isChecked)
        }
        shareBtn.setOnClickListener {
            vm.shareBtn()
        }
        supportBtn.setOnClickListener {
            vm.supportBtn()
        }
        agreementBtn.setOnClickListener {
            vm.agreementBtn()
        }
    }
}