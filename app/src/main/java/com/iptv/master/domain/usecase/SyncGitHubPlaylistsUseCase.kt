package com.iptv.master.domain.usecase

import com.iptv.master.domain.repository.GitHubRepository
import com.iptv.master.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SyncGitHubPlaylistsUseCase @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val playlistRepository: PlaylistRepository
) {
    suspend operator fun invoke(): Result<Map<String, Int>> {
        val results = mutableMapOf<String, Int>()
        val playlists = playlistRepository.getAllPlaylists().first()
        val activePlaylists = playlists.filter { it.isActive }
        for (playlist in activePlaylists) {
            val result = gitHubRepository.syncPlaylistFromGitHub(playlist)
            result.onSuccess { count ->
                results[playlist.id] = count
            }
        }
        return if (results.isNotEmpty()) Result.success(results)
        else Result.failure(Exception("No playlists synced"))
    }
}
