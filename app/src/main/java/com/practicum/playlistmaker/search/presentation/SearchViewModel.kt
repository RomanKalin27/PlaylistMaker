package com.practicum.playlistmaker.search.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.HISTORY_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.LOADING_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NOTHING_FOUND
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NO_CONNECTION
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.SEARCH_RESULTS
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.usecases.GetHistoryUseCase
import com.practicum.playlistmaker.search.domain.usecases.RemoveTracksUseCase
import com.practicum.playlistmaker.search.domain.usecases.SaveTrackUseCase
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveTrackUseCase: SaveTrackUseCase,
    private val removeTracksUseCase: RemoveTracksUseCase,
    private val searchInteractor: SearchInteractor,
) : ViewModel() {
    private var historyList = getHistoryUseCase.execute()
    private var searchScreen = SearchState(SEARCH_RESULTS, historyList)
    private val screenState = MutableLiveData<SearchState>()

    init {
        loadHistory()
    }

    fun returnScreenState(): LiveData<SearchState> {
        return screenState
    }

    fun setState(state: Int) {
        when (state) {
            SEARCH_RESULTS -> {
                screenState.postValue(searchScreen)
            }

            LOADING_STATE -> {
                screenState.postValue(SearchState(LOADING_STATE, historyList))
            }

            NOTHING_FOUND -> {
                screenState.postValue(SearchState(NOTHING_FOUND, historyList))
            }

            NO_CONNECTION -> {
                screenState.postValue(SearchState(NO_CONNECTION, historyList))
            }

            HISTORY_STATE -> {
                historyList = getHistoryUseCase.execute()
                screenState.postValue(SearchState(HISTORY_STATE, historyList))
            }
        }
        returnScreenState()
    }

    fun clearSearchList() {
        searchScreen.searchList.clear()
    }

    fun clearHistoryList() {
        removeTracksUseCase.execute()
        setState(HISTORY_STATE)
    }

    fun loadHistory() {
        setState(HISTORY_STATE)
    }

     fun saveTrack(track: Track, historyList: ArrayList<Track>) {
        saveTrackUseCase.execute(track, historyList)
    }

    fun getTrack(input: String) {
        setState(LOADING_STATE)
        viewModelScope.launch {
            searchInteractor
                .searchTracks(input)
                .collect {
                    clearSearchList()
                    if (it.isNotEmpty()) {
                        searchScreen.searchList.addAll(it)
                        setState(SEARCH_RESULTS)
                    } else {
                        if (searchInteractor.isOnline()) {
                            setState(NOTHING_FOUND)
                        } else {
                            setState(NO_CONNECTION)
                        }
                    }
                }
                }
    }
}

