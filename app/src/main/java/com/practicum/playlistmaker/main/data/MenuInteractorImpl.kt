package com.practicum.playlistmaker.main.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.LibraryActivity
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.settings.presentation.SettingActivity
import com.practicum.playlistmaker.main.domain.api.MenuInteractor

class MenuInteractorImpl(val context: Context) : MenuInteractor {
    override fun searchIntent() {
        val searchIntent = Intent(context, SearchActivity::class.java)
        context.startActivity(searchIntent)
    }

    override fun libraryIntent() {
        val libraryIntent = Intent(context, LibraryActivity::class.java)
        context.startActivity(libraryIntent)
    }

    override fun settingsIntent() {
        val settingsIntent = Intent(context, SettingActivity::class.java)
        context.startActivity(settingsIntent)
    }
}