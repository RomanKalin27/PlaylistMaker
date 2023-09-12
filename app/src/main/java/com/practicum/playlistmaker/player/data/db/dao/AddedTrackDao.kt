package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.player.data.db.entity.AddedTrackEntity

@Dao
interface AddedTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAddedTrack(addedTrackEntity: AddedTrackEntity)

    /*@Query("SELECT * FROM added_track_table where ")
    suspend fun getAddedTracksByIds(ids: List<Long>): List<AddedTrackEntity>*/

    @Query("SELECT * FROM added_track_table ORDER BY dbId DESC")
    suspend fun getAddedTrack(): List<AddedTrackEntity>

    @Query("SELECT * FROM added_track_table WHERE dbId = :trackId")
    suspend fun getAddedTrackById(trackId: Long): AddedTrackEntity

    @Delete(entity = AddedTrackEntity::class)
    suspend fun deleteAddedTrackEntity(addedTrackEntity: AddedTrackEntity)
}