package com.practicum.playlistmaker.search.presentation


import android.os.Handler
import android.os.Looper
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
    private val searchRunnable = Runnable { getTrack(searchInput) }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val historyList = getHistoryUseCase.execute()
    private var searchScreen = SearchState(SEARCH_RESULTS, historyList)
    private var searchInput = ""
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

    private fun saveTrack(track: Track, historyList: ArrayList<Track>) {
        saveTrackUseCase.execute(track, historyList)
    }

    fun searchDebounce(input: String) {
        handler.removeCallbacks(searchRunnable)
        if (input.isNotEmpty()) {
            searchInput = input
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun onSearch(input: String) {
        if (clickDebounce()) {
            getTrack(input)
        }
    }

    fun onClick(track: Track) {
        if (clickDebounce()) {
            saveTrack(track, screenState.value!!.historyList)
        }
    }

    fun getTrack(input: String) {
        setState(LOADING_STATE)
        searchInteractor.searchTracks(input,
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

