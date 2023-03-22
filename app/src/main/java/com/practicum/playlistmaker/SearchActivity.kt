package com.practicum.playlistmaker

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val base_url = "https://itunes.apple.com/"
    private lateinit var searchInput: String
    private lateinit var searchEditText: EditText
    private lateinit var placeholder: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderExtraText: TextView
    private lateinit var refreshBtn: Button
    private lateinit var goBackBtn: ImageButton
    private lateinit var deleteBtn: ImageButton
    private lateinit var recyclerView: RecyclerView
    private val adapter = Adapter()
    private val trackList = ArrayList<Track>()
    private val retrofit = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(TrackInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchEditText = findViewById(R.id.searchBar)
        goBackBtn = findViewById(R.id.goBackBtn)
        goBackBtn.setOnClickListener {
            finish()
        }
        deleteBtn = findViewById(R.id.deleteBtn)
        deleteBtn.isGone = true
        deleteBtn.setOnClickListener() {
            searchEditText.text.clear()
            hideKeyboard()
            trackList.clear()
        }
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
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack(searchInput)
                true
            }
            false
        }
    }

    private fun getTrack(input: String) {
        placeholder = findViewById(R.id.placeholder)
        placeholder.visibility = View.GONE
        recyclerView = findViewById(R.id.recycler_view)
        adapter.trackList = trackList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (input.isNotEmpty()) {
            itunesService.search(input).enqueue(object : Callback<TrackResponse?> {
                override fun onResponse(
                    call: Call<TrackResponse?>,
                    response: Response<TrackResponse?>
                ) {
                    trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                    } else {
                        showPlaceholder(getString(R.string.nothing_found), "", "")
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse?>, t: Throwable) {
                    showPlaceholder(
                        getString(R.string.no_connection),
                        getString(R.string.no_connection_extra),
                        input
                    )
                }
            })
        }
    }

    private fun showPlaceholder(text: String, extraText: String, input: String) {
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderText = findViewById(R.id.placeholderText)
        placeholderExtraText = findViewById(R.id.placeholderExtraText)
        placeholder = findViewById(R.id.placeholder)
        refreshBtn = findViewById(R.id.refresh_btn)
        val mode = baseContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        trackList.clear()
        placeholder.visibility = View.VISIBLE
        refreshBtn.setOnClickListener {
            getTrack(input)
        }
        placeholderText.text = text
        placeholderExtraText.text = extraText
        if (extraText.isEmpty()) {
            refreshBtn.isGone = true
            when (mode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    placeholderImage.setBackgroundResource(R.drawable.ic_nothing_found_night)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    placeholderImage.setBackgroundResource(R.drawable.ic_nothing_found_day)
                }
            }
        } else {
            refreshBtn.isGone = false
            when (mode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    placeholderImage.setBackgroundResource(R.drawable.ic_no_connection_night)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    placeholderImage.setBackgroundResource(R.drawable.ic_no_connection_day)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(key_input, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchInput = savedInstanceState.getString(key_input, "")
        searchEditText.setText(searchInput)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        const val key_input = "key_input"
    }
}