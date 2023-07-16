package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class SearchViewModel(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveTrackUseCase: SaveTrackUseCase,
    private val removeTracksUseCase: RemoveTracksUseCase,
    private val searchInteractor: SearchInteractor,
) : ViewModel() {
    private var searchInput = ""
    private var searchScreen = SearchState(SEARCH_RESULTS)
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
                screenState.postValue(SearchState(LOADING_STATE))
            }

            NOTHING_FOUND -> {
                screenState.postValue(SearchState(NOTHING_FOUND))
            }

            NO_CONNECTION -> {
                screenState.postValue(SearchState(NO_CONNECTION))
            }

            HISTORY_STATE -> {
                screenState.postValue(SearchState(HISTORY_STATE))
            }
        }
        returnScreenState()
    }

    fun saveInput(input: String) {
        searchInput = input
        screenState.value?.searchInput = input

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

    fun getHistory() {
        screenState.value?.historyList?.addAll(getHistoryUseCase.execute())
    }

    fun saveTrack(track: Track, historyList: ArrayList<Track>) {
        saveTrackUseCase.execute(track, historyList)
    }

    fun getTrack() {
        setState(LOADING_STATE)
        searchInteractor.searchTracks(searchInput,
            object : SearchInteractor.TracksConsumer {
                override fun consume(foundMovies: List<Track>) {
                    clearSearchList()
                    if (foundMovies.isNotEmpty()) {
                        searchScreen.searchList.addAll(foundMovies)
                        setState(SEARCH_RESULTS)
                    } else {
                        if (searchInteractor.isOnline()) {
                            setState(NOTHING_FOUND)
                        } else {
                            setState(NO_CONNECTION)
                        }
                    }
                }
            })
    }
}

