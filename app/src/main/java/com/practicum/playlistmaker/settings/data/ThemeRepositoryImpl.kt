package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.api.ThemeRepository
import com.practicum.playlistmaker.settings.domain.models.Switch
private const val THEME = "THEME"
class ThemeRepositoryImpl(private val sharedPrefs: SharedPreferences) : ThemeRepository {
    override fun saveTheme(state: Switch) {
        sharedPrefs.edit()
            .putBoolean(THEME, state.isChecked)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(THEME, false)
    }
}