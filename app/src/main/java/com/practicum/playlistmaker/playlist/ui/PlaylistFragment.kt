package com.practicum.playlistmaker.playlist.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.presentation.PlaylistViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment(), TrackAdapter.AdapterListener {

    companion object {
        fun newInstance() = PlaylistFragment()
        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(id: Int): Bundle =
            bundleOf(PLAYLIST_ID to id)
    }

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var goBackBtn: ImageButton
    private lateinit var shareBtn: ImageButton
    private lateinit var moreBtn: ImageButton
    private lateinit var playlistArtwork: ImageView
    private lateinit var playlistName: TextView
    private lateinit var year: TextView
    private lateinit var playlistLength: TextView
    private lateinit var numberOfTracks: TextView
    private lateinit var exitDialog: MaterialAlertDialogBuilder
    private lateinit var bsRecycler: RecyclerView
    private var adapter = TrackAdapter(this)
    private lateinit var bottomSheetContainer: LinearLayout
    private var bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
    private val vm by viewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.loadPlaylist(requireArguments().getInt(PLAYLIST_ID))

        goBackBtn = binding.goBackBtn
        playlistArtwork = binding.artwork
        playlistName = binding.playlistName
        year = binding.creationYear
        playlistLength = binding.playlistLength
        numberOfTracks = binding.numberOfTracks
        shareBtn = binding.shareBtn
        moreBtn = binding.optionsBtn
        bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bsRecycler = binding.bottomSheetRecycler
        bsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bsRecycler.adapter = adapter
        exitDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(R.string.playlistDialogTitle)
            .setMessage(R.string.playlistDialogBody)
            .setNeutralButton(R.string.cancel) { dialog, which ->
            }.setPositiveButton(R.string.finish) { dialog, which ->
            }

        goBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        vm.observeState().observe(viewLifecycleOwner) {
            playlistName.text = it.playlistName
            year.text = "2022"
            val length = it.playlistLength.toString() + " " + requireContext().getString(R.string.minutes)
            playlistLength.text = length
            var tracks =""
            when (it.numberOfTracks.toString()) {
                "1" -> tracks = requireContext().getString(R.string.tracks_)
                "2", "3", "4" -> tracks = requireContext().getString(R.string.tracks_a)
                else -> tracks = requireContext().getString(R.string.track_ov)
            }
            val trackNumber = it.numberOfTracks.toString() + " " + tracks
            numberOfTracks.text = trackNumber
            Glide.with(playlistArtwork)
                .load(
                    BitmapFactory.decodeFile(it.artworkPath)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        playlistArtwork.resources.getDimensionPixelSize(R.dimen.artwork_corner_r)
                    )
                )
                .placeholder(R.drawable.ic_player_placeholder)
                .into(playlistArtwork)
            adapter.trackList = it.trackList
        }

        }

    override fun onClick(track: Track) {

    }

}