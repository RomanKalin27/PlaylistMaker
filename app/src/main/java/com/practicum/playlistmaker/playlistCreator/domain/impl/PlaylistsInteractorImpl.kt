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

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistsRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(id: Long) {
        playlistsRepository.deletePlaylist(id)
    }

    override suspend fun addTrackToPlaylist(track: Track) {
        playlistsRepository.addTrackToPlaylist(track)
    }

    override suspend fun update(tracklist: ArrayList<Long>, numberOfTracks: Int, playlistId: Int) {
        playlistsRepository.update(tracklist, numberOfTracks, playlistId)
    }

    override fun saveArtwork(image: View): String {
        return playlistsRepository.saveArtwork(image)
    }
}