package com.practicum.playlistmaker.playlistCreator.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    val playlistName: String,
    val playlistDesc: String?,
    val artworkUri: String?,
    var trackList: String,
    var numberOfTracks: Int,
)