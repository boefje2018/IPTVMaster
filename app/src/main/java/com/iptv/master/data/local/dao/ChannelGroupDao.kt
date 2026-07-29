package com.iptv.master.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iptv.master.data.local.entity.ChannelGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelGroupDao {
    @Query("SELECT * FROM channel_groups ORDER BY name ASC")
    fun getAll(): Flow<List<ChannelGroupEntity>>

    @Query("SELECT * FROM channel_groups WHERE id = :id")
    suspend fun getById(id: Long): ChannelGroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: ChannelGroupEntity): Long

    @Update
    suspend fun update(group: ChannelGroupEntity)

    @Delete
    suspend fun delete(group: ChannelGroupEntity)

    @Query("DELETE FROM channel_groups WHERE id = :id")
    suspend fun deleteById(id: Long)
}
