package com.iptv.master.domain.usecase

import com.iptv.master.domain.model.Playlist
import com.iptv.master.domain.repository.PlaylistRepository
import javax.inject.Inject

class AddPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(playlist: Playlist): Result<Unit> {
        return if (playlist.url != null && playlist.url.isNotBlank()) {
            val urlPattern = Regex("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")
            if (!urlPattern.matches(playlist.url)) {
                Result.failure(IllegalArgumentException("Invalid URL format"))
            } else {
                repository.addPlaylist(playlist)
                Result.success(Unit)
            }
        } else {
            repository.addPlaylist(playlist)
            Result.success(Unit)
        }
    }
}
