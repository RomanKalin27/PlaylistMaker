package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone

class SearchActivity : AppCompatActivity() {
    var searchEditText: EditText? = null
    var searchInput: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val goBackBtn = findViewById<ImageButton>(R.id.goBackBtn)

        goBackBtn.setOnClickListener {
            finish()
        }
        val searchEditText = findViewById<EditText>(R.id.searchBar)
        val deleteBtn = findViewById<ImageButton>(R.id.deleteBtn)
        deleteBtn.isGone = true
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                deleteBtn.isGone = searchEditText.text.toString().trim().isEmpty()
                searchInput = s.toString()
            }
        })

        deleteBtn.setOnClickListener() {
            searchEditText.text.clear()
            hideKeyboard()
        }
    }

    companion object {
        const val key_input = "key_input"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(key_input, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(key_input, "")
        searchEditText?.setText(searchInput)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
