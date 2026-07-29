package com.iptv.master.data.repository

import com.iptv.master.data.local.dao.PlaylistDao
import com.iptv.master.data.local.entity.UserPlaylistEntity
import com.iptv.master.data.remote.GitHubService
import com.iptv.master.data.remote.M3UParser
import com.iptv.master.domain.model.Playlist
import com.iptv.master.domain.model.PlaylistType
import com.iptv.master.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val gitHubService: GitHubService
) : PlaylistRepository {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.insert(playlist.toEntity())
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.update(playlist.toEntity())
    }

    override suspend fun deletePlaylist(id: String) {
        val entity = playlistDao.getById(id)
        if (entity != null) {
            playlistDao.delete(entity)
        }
    }

    override suspend fun syncPlaylist(id: String): Result<Int> {
        return try {
            val entity = playlistDao.getById(id)
                ?: return Result.failure(Exception("Playlist not found"))
            val playlist = entity.toDomain()
            val url = playlist.url ?: return Result.failure(Exception("No URL"))
            val response = gitHubService.fetchRawContent(url)
            val content = response.string()
            val channels = M3UParser.parse(content, id)
            val updated = entity.copy(
                channelCount = channels.size,
                lastSynced = System.currentTimeMillis()
            )
            playlistDao.update(updated)
            Result.success(channels.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylistById(id: String): Playlist? {
        return playlistDao.getById(id)?.toDomain()
    }

    private fun UserPlaylistEntity.toDomain(): Playlist {
        return Playlist(
            id = id,
            name = name,
            url = url,
            playlistType = try { PlaylistType.valueOf(playlistType) } catch (e: Exception) { PlaylistType.M3U_URL },
            serverUrl = serverUrl,
            username = username,
            password = password,
            macAddress = macAddress,
            isActive = isActive,
            lastSynced = lastSynced,
            channelCount = channelCount,
            createdAt = createdAt
        )
    }

    private fun Playlist.toEntity(): UserPlaylistEntity {
        return UserPlaylistEntity(
            id = id,
            name = name,
            url = url,
            playlistType = playlistType.name,
            serverUrl = serverUrl,
            username = username,
            password = password,
            macAddress = macAddress,
            isActive = isActive,
            lastSynced = lastSynced,
            channelCount = channelCount,
            createdAt = createdAt
        )
    }
}
