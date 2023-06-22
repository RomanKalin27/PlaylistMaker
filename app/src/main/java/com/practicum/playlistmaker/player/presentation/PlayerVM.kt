package com.practicum.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.PlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.models.PlayerState
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerVM(
    private val player: PlayerInteractorImpl,
    private val trackRepository: TrackRepositoryImpl,
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val setTimeRunnable = Runnable { getPlayerState() }
    private val screenState = MutableLiveData<PlayerState>()
    private val screen = PlayerState()

    init {
        loadTrack()
        setTimeRunnable.run()
    }

    fun returnScreenState(): LiveData<PlayerState> {
        screenState.value = screen
        return screenState
    }

    private fun loadTrack() {
        screen.track = Track(
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
        returnScreenState()
    }

    private fun getPlayerState() {
        screen.playerState = player.returnPlayerState()
        setTime()
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
        returnScreenState()
    }

    private fun setTime() {
        screen.timePlayed = player.setTime()
        returnScreenState()
    }

    fun getArtwork(artwork: ImageView) {
        Glide.with(artwork)
            .load(
                trackRepository.getArtwork()
                    ?.replaceAfterLast('/', "512x512bb.jpg")
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .transform(RoundedCorners(8))
            .into(artwork)
    }


    fun preparePlayer() {
        player.preparePlayer()
    }

    fun playbackControl() {
        player.playbackControl()
    }

    fun onDestroy() {
        handler.removeCallbacks(setTimeRunnable)
        player.onDestroy()
    }

    companion object {
        private const val SET_TIME_DELAY = 400L

    }
}