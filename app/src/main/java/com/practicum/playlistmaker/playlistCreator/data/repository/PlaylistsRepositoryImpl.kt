package com.practicum.playlistmaker.playlistCreator.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.data.TrackDbConvertor
import com.practicum.playlistmaker.library.data.db.AppDatabase
import com.practicum.playlistmaker.player.data.db.entity.AddedTrackEntity
import com.practicum.playlistmaker.playlistCreator.data.db.PlaylistDbConvertor
import com.practicum.playlistmaker.playlistCreator.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.playlistCreator.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.playlistCreator.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor:PlaylistDbConvertor,
    private val trackDbConvertor: TrackDbConvertor,
    private val gson: Gson,
    private val context: Context,
) : PlaylistsRepository {

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun getPlaylistById(playlistId: Int): Playlist {
        return playlistDbConvertor.map(appDatabase.playlistDao().getPlaylistById(playlistId))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    override suspend fun deletePlaylist(id: Int) {
        appDatabase.playlistDao()
            .deletePlaylistEntity(appDatabase.playlistDao().getPlaylistById(id))
    }

    override suspend fun addTrackToPlaylist(track: Track) {
        val addedTrackEntity = AddedTrackEntity(
            dbId = track.trackId,
            trackId = track.trackId, trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite,
        )
        appDatabase.addedTrackDao().insertAddedTrack(addedTrackEntity)
    }

    override suspend fun getAddedTracks(ids: List<Long>): ArrayList<Track> {
        val trackList = ArrayList<Track>()
        ids.forEach { trackList.add(trackDbConvertor.map_added(appDatabase.addedTrackDao().getAddedTrackById(it))) }
        return trackList
    }

    override suspend fun update(tracklist: ArrayList<Long>, numberOfTracks: Int, playlistId: Int) {
        appDatabase.playlistDao().update(gson.toJson(tracklist), numberOfTracks, playlistId)
    }

    override fun saveArtwork(image: View): String {
        val imageFromView = getBitmapFromView(image)
        var imageFile = context.getDir(context.getString(R.string.images), Context.MODE_PRIVATE)
        imageFile = File(imageFile, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(imageFile)
        imageFromView.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        return imageFile.path
    }

        fun getBitmapFromView(view: View): Bitmap {
            return Bitmap.createBitmap(view.width, view.height,Bitmap.Config.ARGB_8888).apply {
                Canvas(this).apply {
                    view.draw(this)
                }
            }
        }


    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }
}