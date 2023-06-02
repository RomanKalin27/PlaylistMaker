package com.practicum.playlistmaker.presentation.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.practicum.playlistmaker.R

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        val goBackBtn = findViewById<ImageButton>(R.id.goBackBtn)

        goBackBtn.setOnClickListener {
            finish()
        }
    }
}