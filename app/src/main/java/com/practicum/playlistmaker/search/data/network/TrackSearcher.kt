package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.Response.Companion.BAD_REQUEST_CODE
import com.practicum.playlistmaker.search.data.dto.Response.Companion.NO_CONNECTION_CODE
import com.practicum.playlistmaker.search.data.dto.Response.Companion.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackSearcher(private val context: Context, private val itunesService: TrackApi) :
    NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isOnline()) {
            return Response().apply { resultCode = NO_CONNECTION_CODE }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = BAD_REQUEST_CODE }
        }

        return withContext(Dispatchers.IO) {
            val response = itunesService.searchNames(dto.expression)
            response.apply { resultCode = SUCCESS_CODE }
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}
