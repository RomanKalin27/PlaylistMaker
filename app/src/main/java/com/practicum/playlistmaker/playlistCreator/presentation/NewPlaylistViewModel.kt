package com.practicum.playlistmaker.playlistCreator.presentation

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.playlistCreator.domain.models.CreatorState
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private var artworkPath = ""
    private val _stateLiveData = MutableLiveData<CreatorState>()
    private val screenState = CreatorState(false, "", "", "")
    var playlist = Playlist(0, "", "", "", ArrayList(), 0)
    fun observeState(): LiveData<CreatorState> = _stateLiveData

    fun editScreen(playlistId: Int) {
        viewModelScope.launch {
            playlist = playlistsInteractor.getPlaylistById(playlistId)
            screenState.titleChanged = true
            screenState.artworkPath = playlist.artworkUri
            screenState.playlistName = playlist.playlistName
            screenState.playlistDesc = playlist.playlistDesc
            _stateLiveData.postValue(screenState)
            observeState()
        }

    }

    fun onArtworkClick(path: Uri, playlistArtwork: ImageView, fakeArtwork: ImageView) {
        Glide.with(fakeArtwork)
            .load(
                path
            )
            .transform(
                CenterCrop(),
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .into(fakeArtwork)

        Glide.with(playlistArtwork)
            .load(
                path
            )
            .transform(
                CenterCrop(),
                RoundedCorners(
                    playlistArtwork.resources.getDimensionPixelSize(R.dimen.artwork_corner_r)
                )
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .into(playlistArtwork)
    }

    fun onCreateBtnClick(playlistName: String, playlistDesc: String) {
        val newPlaylist = Playlist(
            dbId = 0,
            playlistName = playlistName,
            playlistDesc = playlistDesc,
            artworkUri = artworkPath,
            numberOfTracks = 0,
            trackList = ArrayList()
        )
        viewModelScope.launch {
            playlistsInteractor.addPlaylist(newPlaylist)
        }
    }

    fun updatePlaylist(playlistName: String, playlistDesc: String) {
        val newPlaylist = Playlist(
            dbId = playlist.dbId,
            playlistName = playlistName,
            playlistDesc = playlistDesc,
            artworkUri = artworkPath,
            numberOfTracks = playlist.numberOfTracks,
            trackList = playlist.trackList
        )
        viewModelScope.launch {
            playlistsInteractor.update(newPlaylist)
        }
    }

    fun loadArtwork(playlistArtwork: ImageView, fakeArtwork: ImageView, path: String?) {
        Glide.with(fakeArtwork)
            .load(
                path
            )
            .transform(
                CenterCrop(),
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .into(fakeArtwork)

        Glide.with(playlistArtwork)
            .load(
                path
            )
            .transform(
                CenterCrop(),
                RoundedCorners(
                    playlistArtwork.resources.getDimensionPixelSize(R.dimen.artwork_corner_r)
                )
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .into(playlistArtwork)
    }

    fun saveArtwork(fakeArtwork: View) {
        artworkPath = playlistsInteractor.saveArtwork(fakeArtwork)
    }
}
