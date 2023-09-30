package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.SearchState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val trackAdapter by lazy { TrackAdapter(::onClick, ::onClick) }
    private lateinit var searchEditText: EditText
    private lateinit var deleteBtn: ImageButton
    private lateinit var searchRecycler: RecyclerView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var searchHistory: ConstraintLayout
    private lateinit var placeholder: LinearLayout
    private lateinit var placeholderText: TextView
    private lateinit var placeholderExtraText: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var refreshBtn: Button
    private var isClickAllowed = true
    private lateinit var onClickDebounce: (Boolean) -> Unit
    private lateinit var onSearchDebounce: (String) -> Unit

    private val vm by viewModel<SearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
        placeholder = binding.placeholder
        placeholderText = binding.placeholderText
        refreshBtn = binding.refreshBtn
        placeholderExtraText = binding.placeholderExtraText
        placeholderImage = binding.placeholderImage

        onClickDebounce = debounce(CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false) {
            isClickAllowed = true
        }

        onSearchDebounce =
            debounce(SEARCH_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, true) {
                if (it.isNotEmpty()) {
                    vm.getTrack(it)
                }
            }

        if (savedInstanceState != null) {
            searchEditText.setText(savedInstanceState.getString(SAVE_INPUT, ""))
        }

        vm.returnScreenState().observe(viewLifecycleOwner) {
            searchRecycler.isVisible = false
            binding.progressBar.isVisible = false
            placeholder.isVisible = false
            searchHistory.isVisible = false
            isClickAllowed = it.isClickAllowed
            when (it.state) {
                SearchState.SEARCH_RESULTS -> {
                    trackAdapter.trackList = it.searchList
                    trackAdapter.notifyDataSetChanged()
                    searchRecycler.isVisible = true
                }

                SearchState.LOADING_STATE -> {
                    binding.progressBar.isVisible = true
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
                    if (it.historyList.isNotEmpty()) {
                        trackAdapter.historyList = it.historyList
                        trackAdapter.trackList = trackAdapter.historyList
                        searchHistory.isVisible = true
                    }
                }
            }
        }

        searchRecycler.adapter = trackAdapter
        searchRecycler.layoutManager = LinearLayoutManager(requireContext())

        historyRecycler.adapter = trackAdapter
        historyRecycler.layoutManager = LinearLayoutManager(requireContext())

        refreshBtn.setOnClickListener {
            vm.getTrack(searchEditText.text.toString())
        }

        deleteBtn.isGone = true
        deleteBtn.setOnClickListener {
            searchEditText.text.clear()
            vm.loadHistory()
        }

        binding.clearHistory.setOnClickListener {
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
                onSearchDebounce(searchEditText.text.toString())
                deleteBtn.isGone = searchEditText.text.toString().trim().isEmpty()
            }
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (isClickAllowed && actionId == EditorInfo.IME_ACTION_DONE) {
                isClickAllowed = false
                onClickDebounce(isClickAllowed)
                vm.getTrack(searchEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun onClick(track: Track) {
        if (isClickAllowed) {
            isClickAllowed = false
            onClickDebounce(isClickAllowed)
            vm.saveTrack(track, trackAdapter.historyList)
            val trackIntent = Intent(requireContext(), PlayerActivity::class.java)
            trackIntent.putExtra(TRACK, track)
            requireContext().startActivity(trackIntent)
            trackAdapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_INPUT, searchEditText.text.toString())
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SAVE_INPUT = "SAVE_INPUT"
        const val TRACK = "TRACK"
    }
}
