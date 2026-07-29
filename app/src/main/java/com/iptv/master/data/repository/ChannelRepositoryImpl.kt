package com.iptv.master.data.repository

import com.iptv.master.data.local.dao.FavoriteDao
import com.iptv.master.data.local.dao.HistoryDao
import com.iptv.master.data.local.dao.PlaylistDao
import com.iptv.master.data.local.entity.FavoriteChannelEntity
import com.iptv.master.data.local.entity.WatchHistoryEntity
import com.iptv.master.data.remote.M3UParser
import com.iptv.master.data.remote.GitHubService
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val historyDao: HistoryDao,
    private val playlistDao: PlaylistDao,
    private val gitHubService: GitHubService
) : ChannelRepository {

    private val _channelCache = MutableStateFlow<List<Channel>>(emptyList())

    init {
        refreshCache()
    }

    suspend fun refreshCache() {
        try {
            val playlists = playlistDao.getAll().first()
            val favorites = favoriteDao.getAll().first()
            val favoriteIds = favorites.map { it.channelId }.toSet()

            val allChannels = playlists.flatMap { playlist ->
                if (playlist.url.isNullOrEmpty()) return@flatMap emptyList()
                try {
                    val response = gitHubService.fetchRawContent(playlist.url)
                    val content = response.string()
                    M3UParser.parse(content, playlist.id)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            _channelCache.value = allChannels.map { it.copy(isFavorite = it.id in favoriteIds) }
        } catch (_: Exception) {
            _channelCache.value = emptyList()
        }
    }

    override fun getAllChannels(): Flow<List<Channel>> {
        return combine(
            _channelCache,
            favoriteDao.getAll()
        ) { channels, favorites ->
            val favoriteIds = favorites.map { it.channelId }.toSet()
            channels.map { it.copy(isFavorite = it.id in favoriteIds) }
        }
    }

    override fun getChannelsByCategory(category: String): Flow<List<Channel>> {
        return getAllChannels().map { channels ->
            channels.filter { it.category == category || it.groupTitle == category }
        }
    }

    override fun getFavoriteChannels(): Flow<List<Channel>> {
        return combine(
            _channelCache,
            favoriteDao.getAll()
        ) { channels, favorites ->
            val favoriteIds = favorites.map { it.channelId }.toSet()
            channels.filter { it.id in favoriteIds }.map { it.copy(isFavorite = true) }
        }
    }

    override fun searchChannels(query: String): Flow<List<Channel>> {
        return getAllChannels().map { channels ->
            val q = query.lowercase()
            channels.filter {
                it.name.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.groupTitle.lowercase().contains(q)
            }
        }
    }

    override suspend fun toggleFavorite(channelId: String) {
        val isFav = favoriteDao.isFavorite(channelId).first()
        if (isFav) {
            favoriteDao.delete(channelId)
        } else {
            favoriteDao.insert(FavoriteChannelEntity(channelId = channelId))
        }
    }

    override suspend fun addToHistory(channelId: String) {
        val channel = _channelCache.value.find { it.id == channelId }
        if (channel != null) {
            historyDao.insert(
                WatchHistoryEntity(
                    channelId = channel.id,
                    channelName = channel.name,
                    channelLogo = channel.logoUrl
                )
            )
        }
    }

    override fun getWatchHistory(): Flow<List<Channel>> {
        return historyDao.getAll().map { history ->
            val cache = _channelCache.value
            history.mapNotNull { entry ->
                cache.find { it.id == entry.channelId }
            }
        }
    }

    override suspend fun clearHistory() {
        historyDao.deleteAll()
    }
}
