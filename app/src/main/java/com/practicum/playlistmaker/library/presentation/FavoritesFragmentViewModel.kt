package com.practicum.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.library.presentation.favorites.FavoritesState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData
    fun fillData() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favoritesInteractor
                .favoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoritesState.Empty())
        } else {
            renderState(FavoritesState.Content(tracks))
        }
    }

    private fun renderState(state: FavoritesState) {
        stateLiveData.postValue(state)
    }
}