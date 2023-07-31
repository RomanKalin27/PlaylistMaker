package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingBinding
import com.practicum.playlistmaker.settings.presentation.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var nightModeSwitch: SwitchCompat
    private lateinit var shareBtn: Button
    private lateinit var supportBtn: Button
    private lateinit var agreementBtn: Button
    private val vm by viewModel<SettingViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nightModeSwitch = binding.NightModeSwitch
        shareBtn = binding.shareBtn
        supportBtn = binding.supportBtn
        agreementBtn = binding.agreementBtn
        vm.getSwitchResult().observe(viewLifecycleOwner) {
            nightModeSwitch.isChecked = it
        }
        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            vm.saveTheme(isChecked)
        }
        shareBtn.setOnClickListener {
            vm.shareBtn()
        }
        supportBtn.setOnClickListener {
            vm.supportBtn()
        }
        agreementBtn.setOnClickListener {
            vm.agreementBtn()
        }
    }
}