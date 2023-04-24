package com.practicum.playlistmaker

import androidx.recyclerview.widget.DiffUtil
import org.chromium.base.Callback

class DiffUtil(
    private val oldList: ArrayList<Track>,
    private val newList: ArrayList<Track>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].trackId != newList[newItemPosition].trackId -> {
                false
            }
            else -> true
        }
    }

}