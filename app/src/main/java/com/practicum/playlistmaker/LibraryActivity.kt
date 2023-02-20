package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        val goBackBtn = findViewById<ImageButton>(R.id.goBackBtn)

        goBackBtn.setOnClickListener {
            val goBackIntent = Intent(this, MainActivity::class.java)
            startActivity(goBackIntent)
            finish()
        }
    }
}