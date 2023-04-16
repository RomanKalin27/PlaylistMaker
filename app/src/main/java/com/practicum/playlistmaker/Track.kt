package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: String,
                 val artworkUrl100: String)