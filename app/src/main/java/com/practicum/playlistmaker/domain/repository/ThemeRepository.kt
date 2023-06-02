package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Switch

interface ThemeRepository {
    fun saveTheme(state: Switch)
    fun getTheme(): Boolean
}