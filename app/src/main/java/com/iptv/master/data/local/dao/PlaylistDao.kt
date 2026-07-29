package com.iptv.master.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iptv.master.data.local.entity.UserPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM user_playlists ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UserPlaylistEntity>>

    @Query("SELECT * FROM user_playlists WHERE id = :id")
    suspend fun getById(id: String): UserPlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: UserPlaylistEntity)

    @Update
    suspend fun update(playlist: UserPlaylistEntity)

    @Delete
    suspend fun delete(playlist: UserPlaylistEntity)
}
