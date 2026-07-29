package com.iptv.master.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iptv.master.data.local.dao.ChannelGroupDao
import com.iptv.master.data.local.dao.FavoriteDao
import com.iptv.master.data.local.dao.HistoryDao
import com.iptv.master.data.local.dao.PlaylistDao
import com.iptv.master.data.local.dao.ReminderDao
import com.iptv.master.data.local.dao.SettingsDao
import com.iptv.master.data.local.entity.ChannelGroupEntity
import com.iptv.master.data.local.entity.FavoriteChannelEntity
import com.iptv.master.data.local.entity.ReminderEntity
import com.iptv.master.data.local.entity.SettingsEntity
import com.iptv.master.data.local.entity.UserPlaylistEntity
import com.iptv.master.data.local.entity.WatchHistoryEntity

@Database(
    entities = [
        FavoriteChannelEntity::class,
        WatchHistoryEntity::class,
        UserPlaylistEntity::class,
        ChannelGroupEntity::class,
        ReminderEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun historyDao(): HistoryDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun channelGroupDao(): ChannelGroupDao
    abstract fun reminderDao(): ReminderDao
    abstract fun settingsDao(): SettingsDao
}
