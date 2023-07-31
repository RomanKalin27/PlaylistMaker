package com.practicum.playlistmaker.search.data.repository

import com.bumptech.glide.load.engine.Resource
import com.practicum.playlistmaker.search.data.dto.Response.Companion.NO_CONNECTION_CODE
import com.practicum.playlistmaker.search.data.dto.Response.Companion.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    var isOnline = true
    override fun searchTracks(expression: String): Flow<List<Track>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            SUCCESS_CODE -> {

                isOnline = true
                emit((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl,
                    )
                })
            }

            NO_CONNECTION_CODE -> {
                isOnline = false
                emit(emptyList())
            }

            else -> {
                isOnline = true
                emit(emptyList())
            }
        }
    }

    override fun checkConnection(): Boolean {
        return isOnline
    }
}
