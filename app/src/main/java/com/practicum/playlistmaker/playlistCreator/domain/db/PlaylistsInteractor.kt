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
    suspend fun addTrackToPlaylist(track: Track)
    suspend fun getAddedTracksById(ids: List<Long>) : ArrayList<Track>
    suspend fun update(tracklist: ArrayList<Long>, numberOfTracks: Int, playlistId: Int)
    fun saveArtwork(image: View): String
}