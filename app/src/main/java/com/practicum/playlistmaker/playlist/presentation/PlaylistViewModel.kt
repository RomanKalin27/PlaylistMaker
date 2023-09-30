package com.practicum.playlistmaker.playlist.presentation

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.models.PlaylistState
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    var playlistId = 0
    var playlist = Playlist(0, "", "", "", ArrayList(), 0)
    private val _stateLiveData = MutableLiveData<PlaylistState>()
    private var screenState = PlaylistState(
        0, "", "", "", 0, 0, ArrayList(), false
    )

    fun observeState(): LiveData<PlaylistState> = _stateLiveData


    fun loadPlaylist(id: Int) {
        viewModelScope.launch {
            playlistId = id
            playlist = playlistsInteractor.getPlaylistById(id)
            screenState.playlistId = playlist.dbId
            screenState.playlistName = playlist.playlistName
            screenState.playlistDesc = playlist.playlistDesc ?: ""
            screenState.artworkPath = playlist.artworkUri ?: ""
            screenState.numberOfTracks = playlist.numberOfTracks
            screenState.trackList = playlistsInteractor.getAddedTracksById(playlist.trackList.reversed())
            playlistLength()
            _stateLiveData.postValue(screenState)
        }
    }

    fun glideArtwork(artwork: ImageView, roundedCorners: Boolean) {
        if (roundedCorners) {
            Glide.with(artwork)
                .load(
                    BitmapFactory.decodeFile(screenState.artworkPath)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        artwork.resources.getDimensionPixelSize(R.dimen.artwork_small_corner_r)
                    )
                )
                .placeholder(R.drawable.ic_player_placeholder)
                .into(artwork)
        } else {
            Glide.with(artwork)
                .load(
                    BitmapFactory.decodeFile(screenState.artworkPath)
                )
                .transform(
                    CenterCrop(),
                )
                .placeholder(R.drawable.ic_player_placeholder)
                .into(artwork)
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            val trackIds = ArrayList<Long>()
            playlist.trackList.forEach {
                if (it != track.trackId) {
                    trackIds.add(it)
                }
            }
            screenState.numberOfTracks -= 1
            playlist.trackList = trackIds
            playlist.numberOfTracks = screenState.numberOfTracks
            playlistsInteractor.deleteTrack(playlist, track.trackId)
            screenState.trackList = playlistsInteractor.getAddedTracksById(trackIds.reversed())
            playlistLength()
            _stateLiveData.postValue(screenState)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlistId)
            screenState.navigateUp = true
            _stateLiveData.postValue(screenState)
        }
    }

    private fun playlistLength() {
        var length = 0
        screenState.trackList.forEach {
            if (it.trackTimeMillis !== null) {
                length += it.trackTimeMillis.toInt()
            }
        }
        screenState.playlistLength = length.div(60000)
    }
}