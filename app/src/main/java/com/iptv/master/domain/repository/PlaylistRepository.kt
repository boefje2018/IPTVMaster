package com.iptv.master.domain.repository

import com.iptv.master.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(id: String)
    suspend fun syncPlaylist(id: String): Result<Int>
    suspend fun getPlaylistById(id: String): Playlist?
}
