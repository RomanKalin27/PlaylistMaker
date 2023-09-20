package com.practicum.playlistmaker.player.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.playlistCreator.ui.NewPlaylistFragment
import com.practicum.playlistmaker.playlistCreator.ui.NewPlaylistFragment.Companion.BUNDLE_KEY
import com.practicum.playlistmaker.playlistCreator.ui.NewPlaylistFragment.Companion.KEY
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchFragment.Companion.TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity(), BottomSheetAdapter.AdapterListener {
    private val playBtn: ImageButton by lazy {
        findViewById(R.id.playBtn)
    }
    private val goBackBtn: ImageButton by lazy {
        findViewById(R.id.goBackBtn)
    }
    private val favoriteBtn: ImageButton by lazy {
        findViewById(R.id.favoriteBtn)
    }
    private val addBtn: ImageButton by lazy {
        findViewById(R.id.addBtn)
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
    private val newPlaylistBtn: AppCompatButton by lazy {
        findViewById(R.id.new_playlist_btn)
    }
    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.bottom_sheet_recycler)
    }
    private var playlists = ArrayList<Playlist>()
    private val track by lazy {
        intent.getParcelableExtra<Track>(TRACK)
    }
    private val bottomSheetContainer by lazy {
        findViewById<LinearLayout>(R.id.bottom_sheet)
    }
    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheetContainer)
    }
    private val overlay by lazy {
        findViewById<View>(R.id.overlay)
    }
    private val vm by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        vm.loadTrack(track as Track, favoriteBtn)
        vm.fillData(playlists)

        goBackBtn.setOnClickListener {
            finish()
        }

        playBtn.setOnClickListener {
            vm.onPlayButtonClicked()
        }

        favoriteBtn.setOnClickListener {
            vm.favoriteBtnChange(favoriteBtn, true)
        }

        addBtn.setOnClickListener {
            recyclerView.adapter = BottomSheetAdapter(this, playlists, this)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        newPlaylistBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.playerLayout, NewPlaylistFragment())
                addToBackStack(null)
                commit()
            }
            supportFragmentManager.setFragmentResultListener(KEY, this)
            { key, bundle ->
                val message = bundle.getString(BUNDLE_KEY)
                if (message !== null) {
                    showSnackbar(message)
                    vm.fillData(playlists)
                    recyclerView.adapter = BottomSheetAdapter(this, playlists, this)
                }
            }
        }

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })

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

        trackName.text = track?.trackName
        artistName.text = track?.artistName
        vm.getArtwork(artwork)
        if (track?.trackTimeMillis?.contains(":") == true) {
            trackTime.text = track?.trackTimeMillis
        } else {
            trackTime.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track?.trackTimeMillis?.toInt())
        }
        collectionName.text = track?.collectionName
        if (collectionName.text.contentEquals("${trackName.text} - Single")) {
            collectionName.isVisible = false
            album.isVisible = false
        }
        releaseDate.text = track?.releaseDate?.substring(0, 4)
        primaryGenreName.text = track?.primaryGenreName
        country.text = track?.country
    }

    override fun onPlaylistClick(playlist: Playlist) {
        if (playlist.trackList.contains(track!!.trackId)) {
            showSnackbar(getString(R.string.hasAlreadyBeenAdded) + " " + playlist.playlistName)
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            vm.onPlaylistClick(playlist, track!!)
            showSnackbar(getString(R.string.addedToPlaylist) + " " + playlist.playlistName)
        }
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun showSnackbar(message: String) {
        val snackbar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        val layout = snackbar.view as SnackbarLayout
        layout.setBackgroundResource(R.drawable.rectangle)
        layout.setPadding(0, 0, 0, 0)
        val textView = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextAppearance(R.style.snackBarText)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    override fun onPause() {
        super.onPause()
        vm.onPause()
    }
}
