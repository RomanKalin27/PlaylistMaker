package com.practicum.playlistmaker.playlistCreator.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlistCreator.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY dbId DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE dbId = :playlistId")
    suspend fun getPlaylistById(playlistId: Int): PlaylistEntity

    @Query("UPDATE playlist_table SET trackList = :trackList, numberOfTracks = :numberOfTracks, playlistName = :playlistName, playlistDesc = :playlistDesc, artworkUri = :artworkPath WHERE dbId = :playlistId")
    suspend fun update(
        playlistId: Int,
        trackList: String,
        numberOfTracks: Int,
        playlistName: String,
        playlistDesc: String?,
        artworkPath: String?
    )


    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)
}