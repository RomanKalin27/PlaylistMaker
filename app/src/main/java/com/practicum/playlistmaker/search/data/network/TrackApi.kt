package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
        @GET("/search?entity=song")
        suspend fun searchNames(@Query("term") text: String): TrackSearchResponse
}

