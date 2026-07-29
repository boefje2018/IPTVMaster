package com.iptv.master.domain.repository

import com.iptv.master.domain.model.Playlist

interface GitHubRepository {
    suspend fun fetchPlaylistFromUrl(url: String): Result<String>
    suspend fun getDefaultPlaylists(): List<String>
    suspend fun syncPlaylistFromGitHub(playlist: Playlist): Result<Int>
}
