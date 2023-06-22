package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.Switch

interface ThemeRepository {
    fun saveTheme(state: Switch)
    fun getTheme(): Boolean
}