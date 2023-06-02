package com.practicum.playlistmaker.data.main

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.presentation.library.LibraryActivity
import com.practicum.playlistmaker.presentation.search.SearchActivity
import com.practicum.playlistmaker.presentation.settings.SettingActivity
import com.practicum.playlistmaker.domain.usecase.menu.MenuInteractor

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