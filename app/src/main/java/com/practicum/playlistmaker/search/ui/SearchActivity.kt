package com.practicum.playlistmaker.search.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.HISTORY_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.LOADING_STATE
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NOTHING_FOUND
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.NO_CONNECTION
import com.practicum.playlistmaker.search.domain.models.SearchState.Companion.SEARCH_RESULTS
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), TrackAdapter.AdapterListener {
    private val searchRunnable = Runnable {
        vm.saveInput(searchEditText.text.toString())
        vm.getTrack()
    }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val trackAdapter by lazy { TrackAdapter(vm.saveTrack(), vm.getHistory(), this) }
    private val searchEditText: EditText by lazy {
        findViewById(R.id.searchBar)
    }
    private val goBackBtn: ImageButton by lazy {
        findViewById(R.id.goBackBtn)
    }
    private val deleteBtn: ImageButton by lazy {
        findViewById(R.id.deleteBtn)
    }
    private val searchRecycler: RecyclerView by lazy {
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

    private val vm by viewModel<SearchViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        vm.returnScreenState().observe(this) {
            searchRecycler.isVisible = false
            progressBar.isVisible = false
            placeholder.isVisible = false
            searchHistory.isVisible = false

            when (it.state) {
                SEARCH_RESULTS -> {
                    trackAdapter.trackList = it.searchList
                    trackAdapter.notifyDataSetChanged()
                    searchRecycler.isVisible = true
                }

                LOADING_STATE -> {
                    progressBar.isVisible = true
                }

                NOTHING_FOUND -> {
                    trackAdapter.notifyDataSetChanged()
                    placeholderText.text = getString(R.string.nothing_found)
                    refreshBtn.isGone = true
                    placeholderImage.setBackgroundResource(R.drawable.ic_nothing_found)
                    placeholder.isVisible = true
                }

                NO_CONNECTION -> {
                    placeholderText.text = getString(R.string.nothing_found)
                    placeholderExtraText.text = searchEditText.text.toString()
                    refreshBtn.isGone = false
                    placeholderImage.setBackgroundResource(R.drawable.ic_no_connection)
                    placeholder.isVisible = true
                }

                HISTORY_STATE -> {
                    if (trackAdapter.historyList.isNotEmpty()) {
                        trackAdapter.trackList = trackAdapter.historyList
                        searchHistory.isVisible = true
                    }
                }
            }
        }
        searchRecycler.adapter = trackAdapter
        searchRecycler.layoutManager = LinearLayoutManager(applicationContext)

        historyRecycler.adapter = trackAdapter
        historyRecycler.layoutManager = LinearLayoutManager(applicationContext)
        searchEditText.setText(vm.returnScreenState().value?.searchInput)

        refreshBtn.setOnClickListener {
            vm.saveInput(searchEditText.text.toString())
            vm.getTrack()
        }
        goBackBtn.setOnClickListener {
            finish()
        }
        deleteBtn.isGone = true
        deleteBtn.setOnClickListener() {
            searchEditText.text.clear()
            hideKeyboard()
            vm.loadHistory()
        }

        clearHistory.setOnClickListener() {
            trackAdapter.clearAdapter()
            vm.clearHistoryList()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (searchEditText.hasFocus() && searchEditText.text.isEmpty()) {
                    vm.loadHistory()
                }
                deleteBtn.isGone = searchEditText.text.toString().trim().isEmpty()
                searchDebounce()
            }
        })
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (clickDebounce() && actionId == EditorInfo.IME_ACTION_DONE) {
                vm.saveInput(searchEditText.text.toString())
                vm.getTrack()
                true
            }
            false
        }
    }

    override fun onClick() {
        (clickDebounce())
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (searchEditText.text.isNotEmpty())
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        if (isClickAllowed) {
            isClickAllowed = false
            trackAdapter.isClickable = false
            handler.postDelayed({
                isClickAllowed = true
                trackAdapter.isClickable = true
            }, CLICK_DEBOUNCE_DELAY)
        }
        return isClickAllowed
    }


    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.saveInput(searchEditText.text.toString())
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}