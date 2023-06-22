package com.practicum.playlistmaker.player.domain.api


interface PlayerInteractor {
    fun setTime(): String
    fun onPause()
    fun onDestroy()
    fun playbackControl()
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
fun returnPlayerState(): Int
}