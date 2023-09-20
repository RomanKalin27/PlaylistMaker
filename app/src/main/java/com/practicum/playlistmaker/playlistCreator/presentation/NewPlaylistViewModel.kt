package com.practicum.playlistmaker.playlistCreator.presentation

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private var artworkPath = ""

    fun onArtworkClick(uri: Uri, playlistArtwork: ImageView) {
        Glide.with(playlistArtwork)
            .load(
                uri
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

    fun saveArtwork(image: View) {
        artworkPath = playlistsInteractor.saveArtwork(image)
    }
}
