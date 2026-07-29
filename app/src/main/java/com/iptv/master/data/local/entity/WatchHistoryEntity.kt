package com.iptv.master.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val channelId: String,
    val channelName: String,
    val channelLogo: String? = null,
    val watchedAt: Long = System.currentTimeMillis(),
    val duration: Long = 0
)
