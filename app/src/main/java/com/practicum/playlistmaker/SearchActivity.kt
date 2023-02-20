package com.practicum.playlistmaker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

class SearchActivity : AppCompatActivity() {
    var searchInput: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val goBackBtn = findViewById<ImageButton>(R.id.goBackBtn)

        goBackBtn.setOnClickListener {
            val goBackIntent = Intent(this, MainActivity::class.java)
            startActivity(goBackIntent)
            finish()
        }
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val deleteBtn = findViewById<ImageButton>(R.id.deleteBtn)
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                deleteBtn.isEnabled = searchBar.text.toString().trim().isNotEmpty()
                searchInput = searchBar.text.toString()

            }
        })

        deleteBtn.isEnabled = false
        deleteBtn.setOnClickListener() {
            searchBar.text.clear()
        }
    }

    companion object {
        const val key_input = "key_input"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        outState.putString(key_input, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchBar = findViewById<EditText>(R.id.searchBar)
        searchInput = savedInstanceState.getString(key_input, "")
    }

}
