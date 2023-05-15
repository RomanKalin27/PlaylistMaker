package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private val baseUrl = "https://itunes.apple.com/"
    private val trackAdapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private val trackList = ArrayList<Track>()
    private val searchRunnable = Runnable { getTrack(searchEditText.text.toString()) }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private val searchEditText: EditText by lazy {
        findViewById(R.id.searchBar)
    }
    private val searchInput: String by lazy {
        searchEditText.text.toString()
    }
    private val goBackBtn: ImageButton by lazy {
        findViewById(R.id.goBackBtn)
    }
    private val deleteBtn: ImageButton by lazy {
        findViewById(R.id.deleteBtn)
    }
    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recycler_view)
    }
    private val historyRecycler: RecyclerView by lazy {
        findViewById(R.id.history_recycler)
    }
    private val searchHistory: ConstraintLayout by lazy {
        findViewById(R.id.search_history)
    }
    private val clearHistory: Button by lazy {
        findViewById(R.id.clear_history)
    }
    private val placeholder: LinearLayout by lazy {
        findViewById(R.id.placeholder)
    }
    private val placeholderText: TextView by lazy {
        findViewById(R.id.placeholderText)
    }
    private val placeholderExtraText: TextView by lazy {
        findViewById(R.id.placeholderExtraText)
    }
    private val placeholderImage: ImageView by lazy {
        findViewById(R.id.placeholderImage)
    }
    private val refreshBtn: Button by lazy {
        findViewById(R.id.refresh_btn)
    }
    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(TrackApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        goBackBtn.setOnClickListener {
            finish()
        }
        deleteBtn.isGone = true
        deleteBtn.setOnClickListener() {
            searchEditText.text.clear()
            hideKeyboard()
            trackList.clear()
        }
        if (App.historyList.isEmpty()) searchHistory.isVisible = false
        historyAdapter.trackList = App.historyList
        if (clickDebounce()) {
            historyRecycler.adapter = historyAdapter
            historyRecycler.layoutManager = LinearLayoutManager(applicationContext)
        }
        clearHistory.setOnClickListener() {
            App.historyList.clear()
            App.sharedPreferences.edit()
                .remove(NEW_TRACK)
                .apply()
            searchHistory.isVisible = false
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchEditText.hasFocus() && searchEditText.text.isEmpty() && App.historyList.isNotEmpty()) {
                    searchHistory.isVisible = true
                    recyclerView.isVisible = false
                } else {
                    searchHistory.isVisible = false
                }
                deleteBtn.isGone = searchEditText.text.toString().trim().isEmpty()
                searchDebounce()
            }
        })
        listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            historyAdapter.notifyDataSetChanged()
        }


        App.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (clickDebounce() && actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack(searchInput)
                true
            }
            false
        }
    }

    private fun getTrack(input: String) {
        trackAdapter.trackList = trackList
        recyclerView.adapter = trackAdapter
        placeholder.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (input.isNotEmpty()) {
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            itunesService.search(input).enqueue(object : Callback<TrackResponse?> {
                override fun onResponse(
                    call: Call<TrackResponse?>,
                    response: Response<TrackResponse?>
                ) {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    trackList.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        trackList.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    } else {
                        progressBar.visibility = View.GONE
                        showPlaceholder(getString(R.string.nothing_found), "", "")
                        trackAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse?>, t: Throwable) {
                    progressBar.visibility = View.GONE
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
        trackList.clear()
        placeholder.visibility = View.VISIBLE
        refreshBtn.setOnClickListener {
            getTrack(input)
        }
        placeholderText.text = text
        placeholderExtraText.text = extraText
        if (extraText.isEmpty()) {
            refreshBtn.isGone = true
            placeholderImage.setBackgroundResource(R.drawable.ic_nothing_found)
        } else {
            refreshBtn.isGone = false
            placeholderImage.setBackgroundResource(R.drawable.ic_no_connection)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(key_input, searchInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText.setText(savedInstanceState.getString(key_input, ""))
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        const val key_input = "key_input"
        const val NEW_TRACK = "NEW_TRACK"
        const val TRACK_NAME = "TRACK_NAME"
        const val ARTIST_NAME = "ARTIST_NAME"
        const val ARTWORK = "ARTWORK"
        const val TRACK_TIME = "TRACK_TIME"
        const val COLLECTION_NAME = "COLLECTION_NAME"
        const val RELEASE_DATE = "RELEASE_DATE"
        const val PRIMARY_GENRE_NAME = "PRIMARY_GENRE_NAME"
        const val COUNTRY = "COUNTRY"
        const val PREVIEW_URL = "PREVIEW_URL"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}