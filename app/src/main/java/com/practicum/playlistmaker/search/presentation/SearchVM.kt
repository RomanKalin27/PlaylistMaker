package com.practicum.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.data.TrackResponse
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.domain.api.TrackApi
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.BLANK_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.HISTORY_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.LOADING_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NOTHING_FOUND
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NO_CONNECTION
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.SEARCH_RESULTS
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.usecases.GetHistoryUseCase
import com.practicum.playlistmaker.search.domain.usecases.RemoveTracksUseCase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchVM(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val removeTracksUseCase: RemoveTracksUseCase,
) : ViewModel() {
    private val trackAdapter = TrackAdapter()
    private val searchList = ArrayList<Track>()
    private var searchInput = String()
    private val baseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(TrackApi::class.java)
    private val screenState = MutableLiveData<SearchState>()

    init {
        trackAdapter.historyList = getHistoryUseCase.execute()
    }

    fun returnScreenState(): LiveData<SearchState> {
        return screenState
    }

    fun setState(state: Int) {
        when (state) {
            SEARCH_RESULTS -> {
                screenState.value = SearchState(SEARCH_RESULTS, trackAdapter)
            }

            LOADING_STATE -> {
                screenState.value = SearchState(LOADING_STATE, trackAdapter)
            }

            NOTHING_FOUND -> {
                screenState.value = SearchState(NOTHING_FOUND, trackAdapter)
            }

            NO_CONNECTION -> {
                screenState.value = SearchState(NO_CONNECTION, trackAdapter)
            }

            HISTORY_STATE -> {
                if (trackAdapter.historyList.isEmpty()) {
                    screenState.value = SearchState(BLANK_STATE, trackAdapter)
                } else {
                    screenState.value = SearchState(HISTORY_STATE, trackAdapter)
                }
            }
        }
        returnScreenState()
    }

    fun saveInput(input: String) {
        searchInput = input
        screenState.value?.searchInput = input
    }

    fun clearSearchList() {
        searchList.clear()
    }

    fun clearHistoryList() {
        trackAdapter.historyList.clear()
        removeTracksUseCase.execute()
        setState(HISTORY_STATE)
    }

    fun loadHistory() {
        trackAdapter.isHistory = true
        trackAdapter.trackList = trackAdapter.historyList
        setState(HISTORY_STATE)
    }

    fun getTrack() {
        trackAdapter.trackList = searchList
        trackAdapter.isHistory = false
        if (searchInput.isNotEmpty()) {
            setState(LOADING_STATE)
            itunesService.search(searchInput).enqueue(object : Callback<TrackResponse?> {
                override fun onResponse(
                    call: Call<TrackResponse?>,
                    response: Response<TrackResponse?>
                ) {
                    clearSearchList()
                    setState(SEARCH_RESULTS)
                    if (response.body()?.results?.isNotEmpty() == true) {
                        searchList.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    } else {
                        setState(NOTHING_FOUND)
                        trackAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse?>, t: Throwable) {
                    setState(NO_CONNECTION)
                }
            })
        }
    }


}