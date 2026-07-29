package com.iptv.master.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iptv.master.data.remote.M3UParser
import com.iptv.master.domain.repository.ChannelRepository
import com.iptv.master.domain.repository.PlaylistRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PlaylistSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val playlistRepository: PlaylistRepository,
    private val channelRepository: ChannelRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            playlistRepository.getAllPlaylists().collect { playlists ->
                val activePlaylists = playlists.filter { it.isActive }
                for (playlist in activePlaylists) {
                    when (playlist.playlistType) {
                        com.iptv.master.domain.model.PlaylistType.M3U_URL -> {
                            playlist.url?.let { url ->
                                val result = playlistRepository.syncPlaylist(playlist.id)
                                if (result.isFailure) {
                                    return@collect Result.retry()
                                }
                            }
                        }
                        com.iptv.master.domain.model.PlaylistType.XTREAM_CODES -> {
                            playlistRepository.syncPlaylist(playlist.id)
                        }
                        com.iptv.master.domain.model.PlaylistType.MAC_PORTAL -> {
                            playlistRepository.syncPlaylist(playlist.id)
                        }
                        else -> {}
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
