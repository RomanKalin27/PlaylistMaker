package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()
    private val setTimeRunnable = Runnable { setTime() }
    private var playerState = STATE_DEFAULT
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        preparePlayer()
        playBtn.setOnClickListener {
            playbackControl()
        }
        goBackBtn.setOnClickListener {
            finish()
        }
        trackName.text = App.sharedPreferences.getString(SearchActivity.TRACK_NAME, null)
        artistName.text = App.sharedPreferences.getString(SearchActivity.ARTIST_NAME, null)
        Glide.with(artwork)
            .load(
                App.sharedPreferences.getString(SearchActivity.ARTWORK, null)
                    ?.replaceAfterLast('/', "512x512bb.jpg")
            )
            .placeholder(R.drawable.ic_player_placeholder)
            .transform(RoundedCorners(8))
            .into(artwork)
        trackTime.text = App.sharedPreferences.getString(SearchActivity.TRACK_TIME, null)
        collectionName.text = App.sharedPreferences.getString(SearchActivity.COLLECTION_NAME, null)
        if (collectionName.text.contentEquals("${trackName.text} - Single")) {
            collectionName.isVisible = false
            album.isVisible = false
        }
        releaseDate.text = App.sharedPreferences.getString(SearchActivity.RELEASE_DATE, null)
        primaryGenreName.text =
            App.sharedPreferences.getString(SearchActivity.PRIMARY_GENRE_NAME, null)
        country.text = App.sharedPreferences.getString(SearchActivity.COUNTRY, null)
    }

    private fun setTime() {
        timePlayed.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacks(setTimeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(setTimeRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        val url = Uri.parse(App.sharedPreferences.getString(SearchActivity.PREVIEW_URL, null))
        mediaPlayer.setDataSource(this, url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playBtn.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play))
            playerState = STATE_PREPARED
            handler.removeCallbacks(setTimeRunnable)
            timePlayed.text = getString(R.string.time)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_pause))
        playerState = STATE_PLAYING
        handler.postDelayed(setTimeRunnable, SET_TIME_DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playBtn.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_play))
        playerState = STATE_PAUSED
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SET_TIME_DELAY = 400L
    }
}