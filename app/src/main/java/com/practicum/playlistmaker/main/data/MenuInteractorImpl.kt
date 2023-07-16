package com.practicum.playlistmaker.main.data

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.library.ui.LibraryActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingActivity
import com.practicum.playlistmaker.main.domain.api.MenuInteractor

class MenuInteractorImpl(val context: Context) : MenuInteractor {
    override fun searchIntent() {
        val searchIntent = Intent(context, SearchActivity::class.java)
        searchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(searchIntent)
    }

    override fun libraryIntent() {
        val libraryIntent = Intent(context, LibraryActivity::class.java)
        libraryIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(libraryIntent)
    }

    override fun settingsIntent() {
        val settingsIntent = Intent(context, SettingActivity::class.java)
        settingsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(settingsIntent)
    }
}