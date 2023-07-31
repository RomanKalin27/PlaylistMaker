package com.practicum.playlistmaker.player.presentation

import android.media.MediaPlayer
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackRepository: TrackRepository,
) : ViewModel() {
    private var timerJob: Job? = null
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    private val url = trackRepository.getPreviewUrl()
    fun observePlayerState(): LiveData<PlayerState> = playerState

    init {
        loadTrack()
        initMediaPlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }

            else -> {}
        }
    }

    private fun initMediaPlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            playerState.postValue(PlayerState.Prepared())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.value = PlayerState.Default()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(TIMER_DELAY)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
    }

    private fun loadTrack() {
        playerState.value?.track = Track(
            0, trackRepository.getTrackName()!!,
            trackRepository.getArtistName()!!,
            trackRepository.getTrackTime()!!,
            trackRepository.getArtwork()!!,
            trackRepository.getCollectionName()!!,
            trackRepository.getReleaseDate()!!,
            trackRepository.getPrimaryGenreName()!!,
            trackRepository.getCountry()!!,
            ""
        )
    }

    fun getArtwork(artwork: ImageView) {
        Glide.with(artwork)
            .load(
                trackRepository.getArtwork()
                    ?.replaceAfterLast('/', "512x512bb.jpg")
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .transform(
                RoundedCorners(
                    artwork.resources.getDimensionPixelSize(R.dimen.artwork_corner_r)
                )
            )
            .into(artwork)
    }

    companion object {
        private const val TIMER_DELAY = 300L
    }
}