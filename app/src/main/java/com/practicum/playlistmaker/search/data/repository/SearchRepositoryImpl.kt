package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    var isOnline = true
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                isOnline = true
                return (response as TrackSearchResponse).results.map {
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
                }
            }

            -1 -> {
                isOnline = false
                return emptyList()
            }

            else -> {
                isOnline = true
                return emptyList()
            }
        }
    }

    override fun checkConnection(): Boolean {
        return isOnline
    }
}
