package com.practicum.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
            val trackIntent = Intent(it.context, PlayerActivity::class.java)
            it.context.startActivity(trackIntent)
        }
    }

    override fun getItemCount() = trackList.size

    fun setData(newTrackList: ArrayList<Track>) {
        val diffUtil = DiffUtil(trackList, newTrackList)
        val diffResults = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        trackList = newTrackList
        diffResults.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artwork: ImageView = itemView.findViewById(R.id.card_icon)
        private val trackName: TextView = itemView.findViewById(R.id.card_track_name)
        private val trackTime: TextView = itemView.findViewById(R.id.card_track_length)
        private val artistName: TextView = itemView.findViewById(R.id.card_artist_name)
        private val historyStrings = HashSet<String>()
        fun bind(item: Track) {
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

            if (App.historyList.contains((item))) {
                App.historyList.remove(item)
            }
            if (App.historyList.size >= historySize) {
                App.historyList.removeAt(9)
            }
            App.historyList.add(0, item)
            App.sharedPreferences.edit()
                .remove(SearchActivity.NEW_TRACK)
                .apply()
            App.historyList.forEach {
                historyStrings.add(createJsonFromTrack(it))
            }
            App.sharedPreferences.edit()
                .putString(SearchActivity.TRACK_NAME, item.trackName)
                .putString(SearchActivity.ARTIST_NAME, item.artistName)
                .putString(SearchActivity.ARTWORK, item.artworkUrl100)
                .putString(SearchActivity.TRACK_TIME, trackTime.text.toString())
                .putString(SearchActivity.COLLECTION_NAME, item.collectionName)
                .putString(SearchActivity.RELEASE_DATE, item.releaseDate.substring(0, 4))
                .putString(SearchActivity.PRIMARY_GENRE_NAME, item.primaryGenreName)
                .putString(SearchActivity.COUNTRY, item.country)
                .putStringSet(SearchActivity.NEW_TRACK, historyStrings)
                .apply()
        }

        private fun createJsonFromTrack(track: Track): String {
            return Gson().toJson(track)
        }

        companion object {
            const val historySize = 10
        }
    }
}

