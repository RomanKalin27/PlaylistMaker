package com.practicum.playlistmaker.search.data.dto

open class Response() {
    var resultCode = DEFAULT_CODE

    companion object {
        const val DEFAULT_CODE = 0
        const val SUCCESS_CODE = 200
        const val BAD_REQUEST_CODE = 400
        const val NO_CONNECTION_CODE = -1
    }
}