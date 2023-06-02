package com.practicum.playlistmaker.data.repository

import android.content.Context
import com.practicum.playlistmaker.domain.models.Switch
import com.practicum.playlistmaker.domain.repository.ThemeRepository

private const val SHARED_PREFS = "SHARED_PREFS"
private const val THEME = "THEME"

class ThemeRepositoryImpl(private val context: Context) : ThemeRepository {
    val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    override fun saveTheme(state: Switch) {
        sharedPrefs.edit()
            .putBoolean(THEME, state.isChecked)
            .apply()
    }

    override fun getTheme(): Boolean {
        return sharedPrefs.getBoolean(THEME, false)
    }
}