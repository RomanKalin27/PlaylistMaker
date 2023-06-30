package com.practicum.playlistmaker.player.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.data.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl

class PlayerVMFactory(context: Context) : ViewModelProvider.Factory {
    private val trackRepository by lazy { TrackRepositoryImpl(context = context) }
    private val player by lazy { PlayerInteractorImpl(context = context) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerVM(
            trackRepository = trackRepository,
            player = player
        ) as T
    }
}