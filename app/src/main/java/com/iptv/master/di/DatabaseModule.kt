package com.iptv.master.di

import android.content.Context
import androidx.room.Room
import com.iptv.master.data.local.AppDatabase
import com.iptv.master.data.local.dao.ChannelGroupDao
import com.iptv.master.data.local.dao.FavoriteDao
import com.iptv.master.data.local.dao.HistoryDao
import com.iptv.master.data.local.dao.PlaylistDao
import com.iptv.master.data.local.dao.ReminderDao
import com.iptv.master.data.local.dao.SettingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "iptv_master_database"
        ).build()
    }

    @Provides
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao = database.historyDao()

    @Provides
    fun providePlaylistDao(database: AppDatabase): PlaylistDao = database.playlistDao()

    @Provides
    fun provideChannelGroupDao(database: AppDatabase): ChannelGroupDao = database.channelGroupDao()

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao = database.reminderDao()

    @Provides
    fun provideSettingsDao(database: AppDatabase): SettingsDao = database.settingsDao()
}
