package com.practicum.playlistmaker.library.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.library.presentation.FavoritesFragmentViewModel
import com.practicum.playlistmaker.library.presentation.favorites.FavoritesState
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchFragment
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(), TrackAdapter.AdapterListener {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val vm by viewModel<FavoritesFragmentViewModel>()
    private var adapter: TrackAdapter? = null

    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var favoritesList: RecyclerView
    private lateinit var progressBar: ProgressBar


    companion object {
        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackAdapter(this)

        placeholderImage = binding.placeholderImage
        placeholderText = binding.placeholderText
        favoritesList = binding.favoritesList
        progressBar = binding.progressBar

        favoritesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favoritesList.adapter = adapter

        vm.fillData()

        vm.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        favoritesList.adapter = null
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Content -> showContent(state.tracks)
            is FavoritesState.Loading -> showLoading()
            is FavoritesState.Empty -> showEmpty()
        }
    }

    private fun showLoading() {
        favoritesList.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeholderText.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        favoritesList.visibility = View.GONE
        placeholderImage.visibility = View.VISIBLE
        placeholderText.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        favoritesList.visibility = View.VISIBLE
        placeholderImage.visibility = View.GONE
        placeholderText.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter?.clearAdapter()
        adapter?.trackList?.addAll(tracks)
        adapter?.notifyDataSetChanged()
    }

    override fun onClick(track: Track) {
        val trackIntent = Intent(requireContext(), PlayerActivity::class.java)
        trackIntent.putExtra(SearchFragment.TRACK, track)
        this.startActivity(trackIntent)
    }

    override fun onResume() {
        super.onResume()
        vm.fillData()
    }
}