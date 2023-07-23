package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.AdapterListener {
    private lateinit var binding: FragmentSearchBinding
    private val searchRunnable = Runnable {
        vm.saveInput(searchEditText.text.toString())
        vm.getTrack()
    }
    private val historyRunnable = Runnable {
        vm.getHistory()
        trackAdapter.historyList = vm.returnScreenState().value!!.historyList
        vm.loadHistory()
    }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val trackAdapter by lazy { TrackAdapter(this) }
    private lateinit var searchEditText: EditText
    private lateinit var deleteBtn: ImageButton
    private lateinit var searchRecycler: RecyclerView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var searchHistory: ConstraintLayout
    private lateinit var clearHistory: Button
    private lateinit var placeholder: LinearLayout
    private lateinit var placeholderText: TextView
    private lateinit var placeholderExtraText: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var refreshBtn: Button
    private lateinit var progressBar: ProgressBar

    private val vm by viewModel<SearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText = binding.searchBar
        deleteBtn = binding.deleteBtn
        searchRecycler = binding.recyclerView
        historyRecycler = binding.historyRecycler
        searchHistory = binding.searchHistory
        clearHistory = binding.clearHistory
        placeholder = binding.placeholder
        placeholderText = binding.placeholderText
        placeholderExtraText = binding.placeholderExtraText
        placeholderImage = binding.placeholderImage
        refreshBtn = binding.refreshBtn
        progressBar = binding.progressBar
        vm.returnScreenState().observe(viewLifecycleOwner) {
            searchRecycler.isVisible = false
            progressBar.isVisible = false
            placeholder.isVisible = false
            searchHistory.isVisible = false

            when (it.state) {
                SearchState.SEARCH_RESULTS -> {
                    trackAdapter.trackList = it.searchList
                    trackAdapter.notifyDataSetChanged()
                    searchRecycler.isVisible = true
                }

                SearchState.LOADING_STATE -> {
                    progressBar.isVisible = true
                }

                SearchState.NOTHING_FOUND -> {
                    trackAdapter.notifyDataSetChanged()
                    placeholderText.text = getString(R.string.nothing_found)
                    placeholderExtraText.isVisible = false
                    refreshBtn.isGone = true
                    placeholderImage.setBackgroundResource(R.drawable.ic_nothing_found)
                    placeholder.isVisible = true
                }

                SearchState.NO_CONNECTION -> {
                    placeholderText.text = getString(R.string.nothing_found)
                    placeholderExtraText.text = searchEditText.text.toString()
                    refreshBtn.isGone = false
                    placeholderExtraText.isVisible = true
                    placeholderImage.setBackgroundResource(R.drawable.ic_no_connection)
                    placeholder.isVisible = true
                }

                SearchState.HISTORY_STATE -> {
                    if (trackAdapter.historyList.isNotEmpty()) {
                        trackAdapter.trackList = trackAdapter.historyList
                        searchHistory.isVisible = true
                    }
                }
            }
        }
        getHistory()
        searchRecycler.adapter = trackAdapter
        searchRecycler.layoutManager = LinearLayoutManager(requireContext())
        historyRecycler.adapter = trackAdapter
        historyRecycler.layoutManager = LinearLayoutManager(requireContext())

        searchEditText.setText(vm.returnScreenState().value?.searchInput)
        refreshBtn.setOnClickListener {
            vm.saveInput(searchEditText.text.toString())
            vm.getTrack()
        }
        deleteBtn.isGone = true
        deleteBtn.setOnClickListener() {
            searchEditText.text.clear()
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

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            vm.saveTrack(track, trackAdapter.historyList)
            val trackIntent = Intent(requireContext(), PlayerActivity::class.java)
            this.startActivity(trackIntent)
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun getHistory() {
        handler.postDelayed(historyRunnable, HISTORY_DELAY)
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (searchEditText.text.isNotEmpty())
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({
                isClickAllowed = true
            }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    override fun onDestroyView() {
        super.onDestroyView()
        vm.saveInput(searchEditText.text.toString())
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val HISTORY_DELAY = 300L
    }
}
