package com.practicum.playlistmaker.playlistCreator.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlistCreator.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT dbId FROM playlist_table")
    suspend fun getPlaylistIds(): List<Long>

    @Query("SELECT * FROM playlist_table ORDER BY dbId DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE dbId = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Query("UPDATE playlist_table SET trackList=:trackList, numberOfTracks=:numberOfTracks WHERE dbId = :playlistId")
    suspend fun update(trackList: String, numberOfTracks: Int, playlistId: Int)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

}