package com.practicum.playlistmaker.player.data

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Looper
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl.Companion.SHARED_PREFS
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(val context: Context) : PlayerInteractor {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Application.MODE_PRIVATE)
    private val mediaPlayer = MediaPlayer()
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
        val url = Uri.parse(TrackRepositoryImpl(context).getPreviewUrl())
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

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_DELAY = 400L
        private const val PLAYER_STATE = "PLAYER_STATE"
    }
}