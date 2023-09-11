package com.practicum.playlistmaker.playlistCreator.data.db

import com.google.gson.Gson
import com.practicum.playlistmaker.playlistCreator.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist

class PlaylistDbConvertor(
    private val gson: Gson,
) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistName = playlist.playlistName,
            playlistDesc = playlist.playlistDesc,
            artworkUri = playlist.artworkUri,
            trackList = createStringFromList(playlist.trackList),
            numberOfTracks = playlist.numberOfTracks
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            dbId = playlist.dbId,
            playlistName = playlist.playlistName,
            playlistDesc = playlist.playlistDesc,
            artworkUri = playlist.artworkUri,
            trackList = createListFromString(playlist.trackList),
            numberOfTracks = playlist.numberOfTracks
        )
    }


    private fun createListFromString(ids: String): ArrayList<Long> {
        val idsList = ArrayList<Long>()
        val idsString = ids.drop(1).dropLast(1)
        if (idsString.isNotEmpty()) {
            idsString.split(",").forEach { idsList.add(it.toLong()) }
        }
        return idsList
    }

    private fun createStringFromList(list: ArrayList<Long>): String {
        return gson.toJson(list)
    }
}