package com.practicum.playlistmaker.playlistCreator.domain.db


import android.view.View
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(playlistId: Int): Playlist
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(id: Int)
    suspend fun getAddedTracksById(ids: List<Long>) : ArrayList<Track>
    suspend fun update(playlist: Playlist)
    suspend fun addTrack(playlist: Playlist, track: Track)
    suspend fun deleteTrack(playlist: Playlist, trackId: Long)
    fun saveArtwork(image: View): String
}