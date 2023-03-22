package com.practicum.playlistmaker

<<<<<<< HEAD
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Track(val trackName: String,
            val artistName: String,
           val trackTimeMillis: String,
            val artworkUrl100: String)
=======
data class Track(val trackName: String,
            var artistName: String,
            var trackTime: String,
            var artworkUrl100: String)
>>>>>>> origin/review
