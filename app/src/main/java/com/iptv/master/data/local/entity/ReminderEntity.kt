package com.iptv.master.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val programId: String,
    val channelId: String,
    val programName: String,
    val channelName: String,
    val startTime: Long,
    val isActive: Boolean = true
)
