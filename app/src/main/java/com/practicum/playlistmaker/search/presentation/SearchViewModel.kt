package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.data.TrackResponse
import com.practicum.playlistmaker.search.domain.api.TrackApi
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.HISTORY_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.LOADING_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NOTHING_FOUND
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NO_CONNECTION
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.SEARCH_RESULTS
import com.practicum.playlistmaker.search.domain.usecases.GetHistoryUseCase
import com.practicum.playlistmaker.search.domain.usecases.RemoveTracksUseCase
import com.practicum.playlistmaker.search.domain.usecases.SaveTrackUseCase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val saveTrackUseCase: SaveTrackUseCase,
    private val removeTracksUseCase: RemoveTracksUseCase,
    private var itunesService: TrackApi
) : ViewModel() {
    private var searchInput = String()
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
                screenState.value = SearchState(SEARCH_RESULTS)
            }

            LOADING_STATE -> {
                screenState.value = SearchState(LOADING_STATE)
            }

            NOTHING_FOUND -> {
                screenState.value = SearchState(NOTHING_FOUND)
            }

            NO_CONNECTION -> {
                screenState.value = SearchState(NO_CONNECTION)
            }

            HISTORY_STATE -> {
                screenState.value = SearchState(HISTORY_STATE)
            }
        }
        returnScreenState()
    }

    fun saveInput(input: String) {
        searchInput = input
        screenState.value?.searchInput = input
    }

    fun clearSearchList() {
        screenState.value?.searchList?.clear()
    }

    fun clearHistoryList() {
        removeTracksUseCase.execute()
        setState(HISTORY_STATE)
    }

    fun loadHistory() {
        setState(HISTORY_STATE)
    }

    fun getHistory(): GetHistoryUseCase {
        return getHistoryUseCase
    }

    fun saveTrack(): SaveTrackUseCase {
        return saveTrackUseCase
    }

    fun getTrack() {
        if (searchInput.isNotEmpty()) {
            setState(LOADING_STATE)
            itunesService.search(searchInput).enqueue(object : Callback<TrackResponse?> {
                override fun onResponse(
                    call: Call<TrackResponse?>,
                    response: Response<TrackResponse?>
                ) {
                    clearSearchList()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        setState(SEARCH_RESULTS)
                        screenState.value?.searchList?.addAll(response.body()?.results!!)
                    } else {
                        setState(NOTHING_FOUND)
                    }
                }

                override fun onFailure(call: Call<TrackResponse?>, t: Throwable) {
                    setState(NO_CONNECTION)
                }
            })
        }
    }


}