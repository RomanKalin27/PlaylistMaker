package com.practicum.playlistmaker.playlistCreator.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.playlistCreator.presentation.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = NewPlaylistFragment()
        const val KEY = "KEY"
        const val BUNDLE_KEY = "BUNDLE_KEY"
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                vm.onArtworkClick(uri, playlistArtwork)
                isArtworkPicked = true
            } else {
                Log.d("PhotoPicker", "Ничего не выбрано")
            }
        }

    private lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var goBackBtn: ImageButton
    private lateinit var playlistArtwork: ImageView
    private lateinit var playlistName: TextInputEditText
    private lateinit var playlistDesc: TextInputEditText
    private lateinit var createBtn: AppCompatButton
    private lateinit var exitDialog: MaterialAlertDialogBuilder
    private var isArtworkPicked = false
    private val vm by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBackBtn = binding.goBackBtn
        playlistArtwork = binding.artworkImage
        playlistName = binding.nameEditText
        playlistDesc = binding.descEditText
        createBtn = binding.createBtn
        exitDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(R.string.playlistDialogTitle)
            .setMessage(R.string.playlistDialogBody)
            .setNeutralButton(R.string.cancel) { dialog, which ->
            }.setPositiveButton(R.string.finish) { dialog, which ->
                setResult(null)
            }

        goBackBtn.setOnClickListener {
            if ((playlistDesc.text.toString().isNotEmpty() || playlistName.text.toString()
                    .isNotEmpty()) || isArtworkPicked
            ) {
                exitDialog.show()
            } else {
                setResult(null)
            }
        }

        playlistArtwork.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        playlistName.addTextChangedListener {
            createBtn.isEnabled = playlistName.text.toString().isNotEmpty()
        }

        createBtn.setOnClickListener {
            if (isArtworkPicked){
                vm.saveArtwork(playlistArtwork)
            }
            vm.onCreateBtnClick(playlistName.text.toString(), playlistDesc.text.toString())
            setResult(playlistName.text.toString())
        }

    }

    private fun setResult(trackName: String?) {
        if (trackName == null) {
            requireActivity().supportFragmentManager.setFragmentResult(
                KEY,
                bundleOf(BUNDLE_KEY to trackName)
            )
        } else {
            val message =
                getString(R.string.playlist) + " " + trackName + " " + getString(R.string.created)
            requireActivity().supportFragmentManager.setFragmentResult(
                KEY,
                bundleOf(BUNDLE_KEY to message)
            )
        }
        fragmentManager?.popBackStack()
    }

}
