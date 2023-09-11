package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.presentation.PlaylistsFragmentViewModel
import com.practicum.playlistmaker.library.presentation.playlists.PlaylistsState
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.playlistCreator.ui.NewPlaylistFragment.Companion.BUNDLE_KEY
import com.practicum.playlistmaker.playlistCreator.ui.NewPlaylistFragment.Companion.KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newPlaylistBtn: AppCompatButton
    private lateinit var playlistRecycler: RecyclerView
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private var adapter: PlaylistAdapter? = null

    private val vm by viewModel<PlaylistsFragmentViewModel>()

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        requireActivity().supportFragmentManager.setFragmentResultListener(KEY, requireActivity())
        { key, bundle ->
            val message = bundle.getString(BUNDLE_KEY)
            if (message !== null) {
                showSnackbar(message)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeholderImage = binding.placeholderImage
        placeholderText = binding.placeholderText
        newPlaylistBtn = binding.newPlaylistBtn
        playlistRecycler = binding.playlistRecycler
        playlistRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        newPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }
        vm.fillData()
        vm.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        playlistRecycler.visibility = View.GONE
        placeholderImage.visibility = View.VISIBLE
        placeholderText.visibility = View.VISIBLE
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistRecycler.visibility = View.VISIBLE
        placeholderImage.visibility = View.GONE
        placeholderText.visibility = View.GONE
        adapter = PlaylistAdapter(playlists, requireContext())
        playlistRecycler.adapter = adapter
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        )
        val layout = snackbar.view as Snackbar.SnackbarLayout
        layout.setBackgroundResource(R.drawable.rectangle)
        layout.setPadding(0, 0, 0, 0)
        val textView = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextAppearance(R.style.snackBarText)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        vm.fillData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        playlistRecycler.adapter = null
    }
}