package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackActivity : AppCompatActivity() {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        goBackBtn.setOnClickListener {
            finish()
        }
        trackName.text = App.sharedPreferences.getString("trackName",null)
        artistName.text = App.sharedPreferences.getString("artistName",null)
        Glide.with(artwork)
            .load(App.sharedPreferences.getString("artwork",null))
            .placeholder(R.drawable.ic_placeholder)
            .transform(RoundedCorners(2))
            .into(artwork)
    }
}