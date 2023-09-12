package com.practicum.playlistmaker.playlist.presentation

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.presentation.playlists.PlaylistsState
import com.practicum.playlistmaker.playlist.domain.models.PlaylistState
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private  val playlistsInteractor: PlaylistsInteractor
): ViewModel() {
    var playlist = Playlist(0, "", "", "", ArrayList(), 0)
    private val stateLiveData = MutableLiveData<PlaylistState>()
    var screenState = PlaylistState("","",0,0,ArrayList())
    fun observeState(): LiveData<PlaylistState> = stateLiveData


    fun loadPlaylist(id: Int){
        viewModelScope.launch {
            val length = 0
            playlist = playlistsInteractor.getPlaylistById(id)
            screenState.playlistName = playlist.playlistName
            screenState.artworkPath = playlist.artworkUri ?: ""
            screenState.numberOfTracks =playlist.numberOfTracks
            screenState.trackList = playlistsInteractor.getAddedTracksById(playlist.trackList)
            screenState.trackList.forEach {
            if (it.trackTimeMillis !== null) {
                length.plus(it.trackTimeMillis.toInt().div(600000))
            }
                screenState.playlistLength = length
                stateLiveData.postValue(screenState)
            }
        }


    }
}