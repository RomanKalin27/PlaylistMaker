package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
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
}