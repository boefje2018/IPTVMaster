package com.iptv.master.data.repository

import com.iptv.master.data.remote.GitHubService
import com.iptv.master.data.remote.M3UParser
import com.iptv.master.domain.model.Playlist
import com.iptv.master.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val gitHubService: GitHubService
) : GitHubRepository {

    companion object {
        private val DEFAULT_PLAYLISTS = listOf(
            "https://raw.githubusercontent.com/iptv-org/iptv/master/index.m3u",
            "https://raw.githubusercontent.com/Free-TV/IPTV/master/playlist/playlist.m3u8"
        )
    }

    override suspend fun fetchPlaylistFromUrl(url: String): Result<String> {
        return try {
            val response = gitHubService.fetchRawContent(url)
            Result.success(response.string())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDefaultPlaylists(): List<String> {
        return DEFAULT_PLAYLISTS
    }

    override suspend fun syncPlaylistFromGitHub(playlist: Playlist): Result<Int> {
        return try {
            val url = playlist.url ?: return Result.failure(Exception("Playlist has no URL"))
            val result = fetchPlaylistFromUrl(url)
            result.map { content ->
                val channels = M3UParser.parse(content, playlist.id)
                channels.size
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
