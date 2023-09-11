package com.practicum.playlistmaker.player.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "added_track_table")
data class AddedTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Long,
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String?,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    val isFavorite: Boolean,
)