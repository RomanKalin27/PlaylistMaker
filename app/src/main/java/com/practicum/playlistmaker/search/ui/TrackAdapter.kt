package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.usecases.SaveTrackUseCase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.usecases.GetHistoryUseCase
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.domain.DiffUtil

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
    var trackList = ArrayList<Track>()
    var historyList = ArrayList<Track>()
    var searchList = ArrayList<Track>()
    var isHistory = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.track_card,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //if (isHistory) {
           // holder.bind(historyList[position])
        //}
        holder.bind(trackList[position])
       /* val trackRepository = TrackRepositoryImpl(context = holder.itemView.context)
        val getHistoryUseCase = GetHistoryUseCase(trackRepository = trackRepository)
        val handler = Handler(Looper.getMainLooper())
        val setRunnable = Runnable { setData(getHistoryUseCase.execute())}
        if(isHistory){
            handler.postDelayed(setRunnable, SET_DATA_DEBOUNCE_DELAY)
        }*/
        holder.itemView.setOnClickListener {
            val trackRepository = TrackRepositoryImpl(context = it.context)
            val saveTrackUseCase = SaveTrackUseCase(trackRepository = trackRepository)
            val getHistoryUseCase = GetHistoryUseCase(trackRepository = trackRepository)
            saveTrackUseCase.execute(trackList[position])
            if(!isHistory) {
                this.notifyDataSetChanged()
            }
            setData(getHistoryUseCase.execute())
            //this.notifyDataSetChanged()
            val trackIntent = Intent(it.context, PlayerActivity::class.java)
            it.context.startActivity(trackIntent)
        }
    }

    override fun getItemCount() = trackList.size


    fun setData(newTrackList: ArrayList<Track>): ArrayList<Track> {
        val diffUtil = DiffUtil(historyList, newTrackList)
        val diffResults = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        historyList = newTrackList
        diffResults.dispatchUpdatesTo(this)
        return historyList
    }
    /*private fun setData2(newTrackList: ArrayList<Track>): ArrayList<Track> {
        val diffUtil = DiffUtil(searchList, newTrackList)
        val diffResults = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        searchList= newTrackList
        diffResults.dispatchUpdatesTo(this)
        return searchList
    }*/


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artwork: ImageView = itemView.findViewById(R.id.card_icon)
        private val trackName: TextView = itemView.findViewById(R.id.card_track_name)
        private val trackTime: TextView = itemView.findViewById(R.id.card_track_length)
        private val artistName: TextView = itemView.findViewById(R.id.card_artist_name)
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
        }
    companion object {
        private const val SET_DATA_DEBOUNCE_DELAY = 1000L
    }
    }

