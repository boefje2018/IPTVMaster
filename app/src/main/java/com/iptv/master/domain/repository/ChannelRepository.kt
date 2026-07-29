package com.iptv.master.domain.repository

import com.iptv.master.domain.model.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun getAllChannels(): Flow<List<Channel>>
    fun getChannelsByCategory(category: String): Flow<List<Channel>>
    fun getFavoriteChannels(): Flow<List<Channel>>
    fun searchChannels(query: String): Flow<List<Channel>>
    suspend fun toggleFavorite(channelId: String)
    suspend fun addToHistory(channelId: String)
    fun getWatchHistory(): Flow<List<Channel>>
    suspend fun clearHistory()
}
