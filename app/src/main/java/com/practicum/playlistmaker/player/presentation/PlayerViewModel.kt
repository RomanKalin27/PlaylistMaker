package com.practicum.playlistmaker.player.presentation

import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.library.domain.db.FavoritesInteractor
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val appDatabase: AppDatabase,
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {
    private var timerJob: Job? = null
    private var playlists = listOf<Playlist>()
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default(playlists))
    private var loadedTrack = Track(0, "", "", "", "", "", "", "", "", "", false)
    fun observePlayerState(): LiveData<PlayerState> = _playerState

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun onPlayButtonClicked() {
        when (_playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun initMediaPlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _playerState.postValue(PlayerState.Prepared(playlists))
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.postValue(PlayerState.Prepared(playlists))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition(), playlists))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition(), playlists))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        _playerState.value = PlayerState.Default(playlists)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_DELAY_MILLIS)
                _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition(), playlists))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    fun loadTrack(track: Track, favoriteBtn: ImageButton) {
        viewModelScope.launch {
            loadedTrack = track
            loadedTrack.isFavorite =
                appDatabase.trackDao().getTracksIds().contains(loadedTrack.trackId)
            favoriteBtnChange(favoriteBtn, false)
        }
        initMediaPlayer(track.previewUrl)
    }

    fun getArtwork(artwork: ImageView) {
        Glide.with(artwork)
            .load(
                loadedTrack.artworkUrl100
                    .replaceAfterLast('/', "512x512bb.jpg")
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .transform(
                RoundedCorners(
                    artwork.resources.getDimensionPixelSize(R.dimen.artwork_corner_r)
                )
            )
            .into(artwork)
    }

    fun favoriteBtnChange(favoriteBtn: ImageButton, isClicked: Boolean) {
        viewModelScope.launch {
            if (loadedTrack.isFavorite) {
                if (isClicked) {
                    loadedTrack.isFavorite = false
                    favoritesInteractor.deleteTrack(loadedTrack.trackId)
                    favoriteBtn.setImageResource(R.drawable.ic_favorite)
                } else {
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_filled)
                }
            } else {
                if (isClicked) {
                    loadedTrack.isFavorite = true
                    favoritesInteractor.addTrack(loadedTrack)
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_filled)
                } else {
                    favoriteBtn.setImageResource(R.drawable.ic_favorite)
                }
            }
        }
    }

    fun onPlaylistClick(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlist.trackList.add(track.trackId)
            playlist.numberOfTracks = playlist.trackList.size
            playlistsInteractor.addTrack(playlist, track)
        }
    }

    fun fillData(playlists: ArrayList<Playlist>) {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect {
                    playlists.clear()
                    playlists.addAll(it)
                }
        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }
}