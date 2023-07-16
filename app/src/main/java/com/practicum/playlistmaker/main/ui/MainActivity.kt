package com.practicum.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.presentation.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val searchButton by lazy { findViewById<Button>(R.id.search_button) }
    private val libraryButton by lazy { findViewById<Button>(R.id.library_button) }
    private val settingsButton by lazy { findViewById<Button>(R.id.settings_button) }
    private val vm by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm.setTheme()
        searchButton.setOnClickListener {
            vm.searchIntent()
        }

        libraryButton.setOnClickListener {
            vm.libraryIntent()
        }

        settingsButton.setOnClickListener {
            vm.settingsIntent()
        }
    }
}