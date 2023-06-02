package com.practicum.playlistmaker.domain.usecase.player


interface PlayerInteractor {
    fun setTime(): String
    fun onPause()
    fun onDestroy()
    fun playbackControl()
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()

}