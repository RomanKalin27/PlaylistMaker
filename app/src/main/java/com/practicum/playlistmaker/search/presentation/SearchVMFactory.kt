package com.practicum.playlistmaker.search.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.usecases.GetHistoryUseCase
import com.practicum.playlistmaker.search.domain.usecases.RemoveTracksUseCase

class SearchVMFactory(context: Context) : ViewModelProvider.Factory {
    private val trackRepository by lazy { TrackRepositoryImpl(context = context) }
    private val getHistoryUseCase by lazy { GetHistoryUseCase(trackRepository = trackRepository) }
    private val removeTracksUseCase by lazy { RemoveTracksUseCase(trackRepository = trackRepository) }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchVM(
            getHistoryUseCase = getHistoryUseCase,
            removeTracksUseCase = removeTracksUseCase,
        ) as T
    }
}