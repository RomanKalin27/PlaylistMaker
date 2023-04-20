package com.practicum.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrackAdapter() : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
    var trackList = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.track_card,
            parent, false
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            holder.saveTrack(trackList[position])
        }
    }

    override fun getItemCount() = trackList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackId: TextView = itemView.findViewById(R.id.card_track_id)
        private val artwork: ImageView = itemView.findViewById(R.id.card_icon)
        private val trackName: TextView = itemView.findViewById(R.id.card_track_name)
        private val trackTime: TextView = itemView.findViewById(R.id.card_track_length)
        private val artistName: TextView = itemView.findViewById(R.id.card_artist_name)
        private val historyStrings = HashSet<String>()
        fun bind(item: Track) {
            trackId.text = item.trackId.toString()
            trackName.text = item.trackName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis.toInt())
            artistName.text = item.artistName
            Glide.with(artwork)
                .load(item.artworkUrl100)
                .placeholder(R.drawable.ic_placeholder)
                .transform(RoundedCorners(2))
                .into(artwork)
        }

        fun saveTrack(item: Track) {
            val track = Track(
                item.trackId,
                item.trackName,
                item.artistName,
                item.trackTimeMillis,
                item.artworkUrl100
            )
            if (App.historyList.contains((track))) {
                App.historyList.remove(track)
            }
            if (App.historyList.size >= 10) {
                App.historyList.removeAt(9)
            }
            App.historyList.add(0, track)
            App.sharedPreferences.edit()
                .remove(SearchActivity.NEW_TRACK)
                .apply()
            App.historyList.forEach {
                historyStrings.add(createJsonFromTrack(it))
            }
            App.sharedPreferences.edit()
                .putStringSet(SearchActivity.NEW_TRACK, historyStrings)
                .apply()
            HistoryAdapter().notifyItemInserted(0)
        }

        private fun createJsonFromTrack(track: Track): String {
            return Gson().toJson(track)
        }
    }
}

