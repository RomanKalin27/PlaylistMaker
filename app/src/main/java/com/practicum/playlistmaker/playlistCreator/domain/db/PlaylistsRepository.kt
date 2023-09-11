package com.practicum.playlistmaker.playlistCreator.domain.db

import android.view.View
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(id: Long)
    suspend fun addTrackToPlaylist(track: Track)
    suspend fun update(tracklist: ArrayList<Long>, numberOfTracks: Int, playlistId: Int)
    fun saveArtwork(image: View): String
}