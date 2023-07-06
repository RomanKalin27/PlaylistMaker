package com.practicum.playlistmaker.player.data

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Looper
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(
    private val context: Context,
    private val sharedPrefs: SharedPreferences,
    private val trackRepository: TrackRepository,
    private val mediaPlayer: MediaPlayer,
) : PlayerInteractor {
    private val handler = android.os.Handler(Looper.getMainLooper())
    private val setTimeRunnable = Runnable { setTime() }
    private var playerState = STATE_DEFAULT
    override fun setTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

    override fun onPause() {
        pausePlayer()
        handler.removeCallbacks(setTimeRunnable)
    }

    override fun onDestroy() {
        mediaPlayer.release()
        handler.removeCallbacks(setTimeRunnable)
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    override fun preparePlayer() {
        val url = Uri.parse(trackRepository.getPreviewUrl())
        mediaPlayer.setDataSource(context, url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            sharedPrefs.edit()
                .putString(PLAYER_STATE, playerState.toString())
                .apply()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            sharedPrefs.edit()
                .putString(PLAYER_STATE, playerState.toString())
                .apply()
            handler.removeCallbacks(setTimeRunnable)
        }
    }


    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        sharedPrefs.edit()
            .putString(PLAYER_STATE, playerState.toString())
            .apply()
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        sharedPrefs.edit()
            .putString(PLAYER_STATE, playerState.toString())
            .apply()

    }

    override fun returnPlayerState(): Int {
        return playerState
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_DELAY = 400L
        private const val PLAYER_STATE = "PLAYER_STATE"
    }
}