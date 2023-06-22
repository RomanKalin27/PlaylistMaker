package com.practicum.playlistmaker.search.domain.api

interface SearchInputRepository {
    fun saveInput(input: String)
    fun getInput(): String?
}