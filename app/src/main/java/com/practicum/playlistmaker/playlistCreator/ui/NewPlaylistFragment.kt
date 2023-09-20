package com.practicum.playlistmaker.playlistCreator.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        const val KEY = "KEY"
        const val BUNDLE_KEY = "BUNDLE_KEY"
        const val EDIT_KEY = "EDIT_KEY"
        fun createArgs(editedPlaylistId: Int): Bundle =
            bundleOf(EDIT_KEY to editedPlaylistId)
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                vm.onArtworkClick(uri, playlistArtwork, fakeArtwork)
                isArtworkPicked = true
                url = uri
            } else {
                Log.d("PhotoPicker", "Ничего не выбрано")
            }
        }

    private lateinit var binding: FragmentNewPlaylistBinding
    private lateinit var playlistArtwork: ImageView
    private lateinit var fakeArtwork: ImageView
    private lateinit var playlistName: TextInputEditText
    private lateinit var playlistDesc: TextInputEditText
    private lateinit var createBtn: AppCompatButton
    private lateinit var exitDialog: MaterialAlertDialogBuilder
    private var isArtworkPicked = false
    private var url = Uri.EMPTY
    private val vm by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistArtwork = binding.artworkImage
        fakeArtwork = binding.fakeArtwork
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

        binding.goBackBtn.setOnClickListener {
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
            if (isArtworkPicked) {
                vm.saveArtwork(fakeArtwork)
            }
            vm.onCreateBtnClick(playlistName.text.toString(), playlistDesc.text.toString())
            setResult(playlistName.text.toString())
        }

        if (requireArguments().getInt(EDIT_KEY) != 0) {
            vm.editScreen(requireArguments().getInt(EDIT_KEY))
            vm.observeState().observe(viewLifecycleOwner) {
                binding.headlineText.text = requireContext().getString(R.string.edit)
                createBtn.text = requireContext().getString(R.string.save)
                playlistName.setText(it.playlistName)
                playlistDesc.setText(it.playlistDesc)
                if (!it.artworkPath.isNullOrEmpty()) {
                    vm.loadArtwork(playlistArtwork, fakeArtwork, it.artworkPath)
                }
                createBtn.setOnClickListener {
                    if (isArtworkPicked) {
                        vm.saveArtwork(fakeArtwork)
                    }
                    vm.updatePlaylist(playlistName.text.toString(), playlistDesc.text.toString())
                    setResult(null)
                }
                binding.goBackBtn.setOnClickListener {
                    setResult(null)
                }
            }
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
