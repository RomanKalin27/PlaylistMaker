package com.practicum.playlistmaker.player.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl.Companion.SHARED_PREFS

class PlayerActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val setTimeRunnable = Runnable { getPlayerState() }
    private val playBtn: ImageButton by lazy {
        findViewById(R.id.playBtn)
    }
    private val goBackBtn: ImageButton by lazy {
        findViewById(R.id.goBackBtn)
    }
    private val artistName: TextView by lazy {
        findViewById(R.id.artistName)
    }
    private val trackName: TextView by lazy {
        findViewById(R.id.trackName)
    }
    private val artwork: ImageView by lazy {
        findViewById(R.id.artwork)
    }
    private val trackTime: TextView by lazy {
        findViewById(R.id.trackTime)
    }
    private val collectionName: TextView by lazy {
        findViewById(R.id.collectionName)
    }
    private val album: TextView by lazy {
        findViewById(R.id.album)
    }
    private val releaseDate: TextView by lazy {
        findViewById(R.id.releaseDate)
    }
    private val primaryGenreName: TextView by lazy {
        findViewById(R.id.primaryGenreName)
    }
    private val country: TextView by lazy {
        findViewById(R.id.country)
    }
    private val timePlayed: TextView by lazy {
        findViewById(R.id.timePlayed)
    }
    private val trackRepository by lazy { TrackRepositoryImpl(context = applicationContext) }
    private val mediaPlayer by lazy { PlayerInteractorImpl(context = applicationContext) }
    private val sharedPrefs by lazy {
        applicationContext.getSharedPreferences(
            SHARED_PREFS,
            Application.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        mediaPlayer.preparePlayer()
        playBtn.setOnClickListener {
            mediaPlayer.playbackControl()
        }
        goBackBtn.setOnClickListener {
            finish()
        }
        trackName.text = trackRepository.getTrackName()
        artistName.text = trackRepository.getArtistName()
        trackRepository.getArtwork(artwork)
        trackTime.text = trackRepository.getTrackTime()
        collectionName.text = trackRepository.getCollectionName()
        if (collectionName.text.contentEquals("${trackName.text} - Single")) {
            collectionName.isVisible = false
            album.isVisible = false
        }
        releaseDate.text = trackRepository.getReleaseDate()
        primaryGenreName.text = trackRepository.getPrimaryGenreName()
        country.text = trackRepository.getCountry()
        setTimeRunnable.run()
    }

    private fun getPlayerState(){
        when (sharedPrefs.getString(PLAYER_STATE, null)?.toInt()) {
            1 -> {
                playBtn.isEnabled = true
                playBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play))
                timePlayed.text = getString(R.string.time)
            }

            2 -> {
                playBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_pause))
                timePlayed.text = mediaPlayer.setTime()
            }

            3 -> {
                playBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play))
            }
        }
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }
    override fun onStop() {
        super.onStop()
        mediaPlayer.onDestroy()
        handler.removeCallbacks(setTimeRunnable)
    }
    companion object {
        private const val PLAYER_STATE = "PLAYER_STATE"
        private const val SET_TIME_DELAY = 400L
    }
}
