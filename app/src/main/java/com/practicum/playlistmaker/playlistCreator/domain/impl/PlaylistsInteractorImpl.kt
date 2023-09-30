package com.practicum.playlistmaker.playlistCreator.domain.impl

import android.view.View
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistsRepository.getPlaylistById(playlistId)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(id: Int) {
        playlistsRepository.deletePlaylist(id)
    }

    override suspend fun getAddedTracksById(ids: List<Long>): ArrayList<Track> {
        return playlistsRepository.getAddedTracks(ids)
    }

    override suspend fun update(playlist: Playlist) {
        playlistsRepository.update(playlist)
    }

    override suspend fun addTrack(
        playlist: Playlist,
        track: Track
    ) {
        playlistsRepository.addTrack(playlist, track)
    }

    override suspend fun deleteTrack(
        playlist: Playlist,
        trackId: Long
    ) {
        playlistsRepository.deleteTrack(playlist, trackId)
    }

    override fun saveArtwork(image: View): String {
        return playlistsRepository.saveArtwork(image)
    }
}