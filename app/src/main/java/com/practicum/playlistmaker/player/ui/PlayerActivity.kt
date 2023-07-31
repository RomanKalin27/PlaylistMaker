package com.practicum.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {
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
    private val vm by viewModel<PlayerViewModel>()
    private val track by lazy { vm.observePlayerState().value!!.track }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        goBackBtn.setOnClickListener {
            finish()
        }
        playBtn.setOnClickListener {
            vm.onPlayButtonClicked()
        }

        vm.observePlayerState().observe(this) {
            playBtn.isEnabled = it.isPlayButtonEnabled
            timePlayed.text = it.progress
            if (it.isPaused) {
                playBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_play
                    )
                )
            } else {
                playBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_pause
                    )
                )
            }
        }

        trackName.text = track.trackName
        artistName.text = track.artistName
        vm.getArtwork(artwork)
        trackTime.text = track.trackTimeMillis
        collectionName.text = track.collectionName
        if (collectionName.text.contentEquals("${trackName.text} - Single")) {
            collectionName.isVisible = false
            album.isVisible = false
        }
        releaseDate.text = track.releaseDate
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
    }


    override fun onPause() {
        super.onPause()
        vm.onPause()
    }
}
