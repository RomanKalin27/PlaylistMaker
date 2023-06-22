package com.practicum.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.presentation.MainVM
import com.practicum.playlistmaker.main.presentation.MainVMFactory

class MainActivity : AppCompatActivity() {
    private val searchButton by lazy { findViewById<Button>(R.id.search_button) }
    private val libraryButton by lazy { findViewById<Button>(R.id.library_button) }
    private val settingsButton by lazy { findViewById<Button>(R.id.settings_button) }
    private val vm by lazy { ViewModelProvider(this, MainVMFactory(this))[MainVM::class.java] }
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