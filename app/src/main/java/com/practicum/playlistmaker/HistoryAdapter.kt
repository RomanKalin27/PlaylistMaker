package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import okhttp3.internal.Util
import java.lang.reflect.Array

class HistoryAdapter() : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
    var historyList: ArrayList<Track> = App.historyList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.track_card,
            parent, false
        )
        return TrackAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackAdapter.ViewHolder, position: Int) {
        holder.bind(historyList[position])
        holder.itemView.setOnClickListener {
            holder.saveTrack(historyList[position])
        }
    }

    override fun getItemCount() = historyList.size
}
