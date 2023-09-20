package com.practicum.playlistmaker.playlist.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.playlist.presentation.PlaylistViewModel
import com.practicum.playlistmaker.playlistCreator.ui.NewPlaylistFragment
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchFragment
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment: Fragment() {

    companion object {

        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(id: Int): Bundle =
          bundleOf(PLAYLIST_ID to id)
    }

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var playlistName: TextView
    private lateinit var playlistNameSheet: TextView
    private lateinit var numberOfTracksSheet: TextView
    private lateinit var playlistArtworkSheet: ImageView
    private lateinit var playlistDesc: TextView
    private lateinit var playlistLength: TextView
    private lateinit var numberOfTracks: TextView
    private lateinit var artwork: ImageView
    private lateinit var noTracksMessage: TextView
    private lateinit var goBackBtn: ImageButton
    private lateinit var shareBtn: ImageButton
    private lateinit var menuBtn: ImageButton
    private lateinit var deleteBtn: Button
    private lateinit var editBtn: Button
    private lateinit var shareBtnBS: Button
    private  lateinit var playlistDialog: MaterialAlertDialogBuilder
    private lateinit var bsRecycler: RecyclerView
    private lateinit var deletedTrack: Track
    private lateinit var adapter: TrackAdapter


    private val vm by viewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = requireArguments().getInt(PLAYLIST_ID)
        vm.loadPlaylist(playlistId)


        noTracksMessage = binding.noTracksMessage
        numberOfTracksSheet = binding.numberOfTracksSheet
        playlistArtworkSheet = binding.playlistArtworkSheet
        editBtn = binding.editBtn
        shareBtnBS = binding.shareBtnBS
        menuBtn = binding.optionsBtn
        shareBtn = binding.shareBtn
        goBackBtn = binding.goBackBtn
        playlistDesc = binding.playlistDesc
        artwork = binding.artwork
        playlistLength = binding.playlistLength
        deleteBtn = binding.deleteBtn
        playlistName = binding.playlistName
        numberOfTracks = binding.numberOfTracks
        playlistNameSheet = binding.playlistNameSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val overlay = binding.overlay
        val bSMenuBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu)
        bSMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bSMenuBehavior.peekHeight = 0
        bSMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })
        adapter = TrackAdapter(::onClick, ::onLongClick)
        bsRecycler = binding.bottomSheetRecycler
        bsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bsRecycler.adapter = adapter



        goBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        shareBtn.setOnClickListener {
            if (adapter.trackList.isNotEmpty()) {
                sharePlaylist()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.noTracksForShare),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        menuBtn.setOnClickListener {
            bSMenuBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        shareBtnBS.setOnClickListener {
            sharePlaylist()
        }
       editBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_newPlaylistFragment,
                NewPlaylistFragment.createArgs(playlistId)
            )
        }
        deleteBtn.setOnClickListener {
            showDialog("${getString(R.string.deletePlaylist)} «${playlistName.text}»?")
        }

        vm.observeState().observe(viewLifecycleOwner) {
            playlistName.text = it.playlistName
            playlistNameSheet.text = it.playlistName

            playlistDesc.text = it.playlistDesc
            if (it.playlistDesc.isEmpty()){
                binding.playlistDesc.visibility = View.GONE
            }

            val length =
                it.playlistLength.toString() + " " + requireContext().getString(R.string.minutes)
            playlistLength.text = length

            numberOfTracks.text = requireContext().resources.getQuantityString(
                R.plurals.tracks, it.numberOfTracks, it.numberOfTracks
            )
            numberOfTracksSheet.text = numberOfTracks.text

            vm.glideArtwork(artwork, false)
            vm.glideArtwork(playlistArtworkSheet, true)

            adapter.trackList = it.trackList
            if (it.trackList.isEmpty()) {
                noTracksMessage.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()

            if(it.navigateUp){
                findNavController().navigateUp()
            }
        }

    }

    private fun onClick(track: Track) {
        val trackIntent = Intent(requireContext(), PlayerActivity::class.java)
        trackIntent.putExtra(SearchFragment.TRACK, track)
        requireContext().startActivity(trackIntent)
    }

    private fun onLongClick(track: Track) {
        deletedTrack = track
        showDialog(getString(R.string.deleteTrack))
    }

    private fun sharePlaylist() {
        val share = Intent(Intent.ACTION_SEND)
        var message = "${numberOfTracks.text}\n"
        var number = 0
        adapter.trackList.forEach {
            number += 1
            message += "${number}. ${it.artistName} - ${it.trackName} (${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTimeMillis?.toInt())
            })\n"
        }
        share.type = "text/plane"
        share.putExtra(Intent.EXTRA_TEXT, message)
        requireContext().startActivity(share)
    }
    private fun showDialog(message: String){
        playlistDialog = MaterialAlertDialogBuilder(requireContext(), R.style.TrackDialogTheme)
            .setMessage(message)
            .setNegativeButton(R.string.no) { dialog, which ->
            }.setPositiveButton(R.string.yes) { dialog, which ->
                if(message == getString(R.string.deleteTrack)){
                    vm.deleteTrack(deletedTrack)
                } else {
                    vm.deletePlaylist()
                }
            }
        playlistDialog.show()
    }


}