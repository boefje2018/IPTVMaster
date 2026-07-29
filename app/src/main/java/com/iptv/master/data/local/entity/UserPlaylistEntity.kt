package com.iptv.master.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_playlists")
data class UserPlaylistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String? = null,
    val playlistType: String = "M3U_URL",
    val serverUrl: String? = null,
    val username: String? = null,
    val password: String? = null,
    val macAddress: String? = null,
    val isActive: Boolean = true,
    val lastSynced: Long? = null,
    val channelCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
