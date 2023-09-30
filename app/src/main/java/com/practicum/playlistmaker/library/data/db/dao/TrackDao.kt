package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksIds(): List<Long>

    @Query("SELECT * FROM track_table ORDER BY dbId DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackEntity(trackEntity: TrackEntity)

}
