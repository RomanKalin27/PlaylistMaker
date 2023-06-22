package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.data.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {
        @GET("/search?entity=song")
        fun search(@Query("term") text: String) : Call<TrackResponse>
}