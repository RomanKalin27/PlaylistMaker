package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist

class BottomSheetAdapter(
    private val adapterListener: AdapterListener,
    val playlists: List<Playlist>,
    val context: Context
) : RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>() {

    interface AdapterListener {
        fun onPlaylistClick(playlist: Playlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_card_bsheet, parent, false)
        return BottomSheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position], context)
        holder.itemView.setOnClickListener {
            adapterListener.onPlaylistClick(playlists[position])
        }
    }

    class BottomSheetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val artwork: ImageView = itemView.findViewById(R.id.playlist_artwork_sheet)
        private val name: TextView = itemView.findViewById(R.id.playlist_name_sheet)
        private val numberOfTracks: TextView = itemView.findViewById(R.id.number_of_tracks_sheet)

        fun bind(playlist: Playlist, context: Context) {
            name.text = playlist.playlistName
            var tracks = ""
            when (playlist.numberOfTracks.toString().last().toString()) {
                "1" -> tracks = context.getString(R.string.tracks_)
                "2", "3", "4" -> tracks = context.getString(R.string.tracks_a)
                else -> tracks = context.getString(R.string.track_ov)
            }
            numberOfTracks.text = "${playlist.numberOfTracks} $tracks"
            Glide.with(artwork)
                .load(
                    // val image: Bitmap =
                    BitmapFactory.decodeFile(playlist.artworkUri)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        artwork.resources.getDimensionPixelSize(R.dimen.artwork_corner_r)
                    )
                )
                .placeholder(R.drawable.ic_player_placeholder)
                .into(artwork)
        }
    }
}