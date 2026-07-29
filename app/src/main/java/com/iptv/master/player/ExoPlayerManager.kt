package com.iptv.master.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.extractor.DefaultExtractorsFactory
import java.io.File

class ExoPlayerManager(context: Context) {

    val player: ExoPlayer
    private val trackSelector: DefaultTrackSelector
    private var retryCount = 0
    private val maxRetries = 3

    init {
        trackSelector = DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters()
                    .setMaxVideoSize(1920, 1080)
                    .setPreferredAudioLanguage("eng")
            )
        }

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
            )
            .setTargetBufferBytes(DefaultLoadControl.DEFAULT_TARGET_BUFFER_BYTES)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = true
                setMediaItem(MediaItem.fromUri(Uri.EMPTY))
            }

        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: Exception) {
                if (retryCount < maxRetries) {
                    retryCount++
                    player.prepare()
                    player.play()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    retryCount = 0
                }
            }
        })
    }

    fun play(url: String) {
        if (url.isBlank()) return
        retryCount = 0
        val uri = Uri.parse(url)
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .setMimeType(detectMimeType(url))
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun playWithCache(url: String, cache: SimpleCache) {
        if (url.isBlank()) return
        retryCount = 0
        val uri = Uri.parse(url)
        val dataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
        player.setMediaSource(mediaSource)
        player.prepare()
        player.play()
    }

    fun pause() { player.pause() }
    fun resume() { player.play() }
    fun seekTo(positionMs: Long) { player.seekTo(positionMs) }
    fun seekForward() { player.seekTo(player.currentPosition + 10_000) }
    fun seekBackward() { player.seekTo(player.currentPosition - 10_000) }
    val currentPosition: Long get() = player.currentPosition
    val duration: Long get() = player.duration

    fun setPlaybackSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }

    fun setVolume(vol: Float) {
        player.volume = vol.coerceIn(0f, 1f)
    }

    fun setAspectRatio(ratio: Int) {
        when (ratio) {
            0 -> {} // fill
            1 -> {} // fit
            2 -> {} // zoom
        }
    }

    fun setQuality(videoHeight: Int) {
        val parameters = trackSelector.parameters.buildUpon()
            .setMaxVideoSize(1920, videoHeight)
        trackSelector.setParameters(parameters)
    }

    fun selectAudioTrack(index: Int) {
        val tracks = player.currentTracks
        val audioTracks = tracks.groups.filter { it.type == C.TRACK_TYPE_AUDIO }
        if (index in audioTracks.indices) {
            audioTracks[index].let { group ->
                player.setTrackSelectionParameters(
                    player.trackSelectionParameters.buildUpon()
                        .setPreferredAudioLanguage(group.supportedTrackInfos.firstOrNull()?.language ?: "")
                        .build()
                )
            }
        }
    }

    fun selectSubtitleTrack(index: Int) {
        val tracks = player.currentTracks
        val subtitleTracks = tracks.groups.filter { it.type == C.TRACK_TYPE_TEXT }
        if (index in subtitleTracks.indices) {
            subtitleTracks[index].let { group ->
                player.setTrackSelectionParameters(
                    player.trackSelectionParameters.buildUpon()
                        .setPreferredTextLanguage(group.supportedTrackInfos.firstOrNull()?.language ?: "")
                        .build()
                )
            }
        }
    }

    private fun detectMimeType(url: String): String? {
        return when {
            url.contains(".m3u8") -> "application/x-mpegURL"
            url.contains(".mpd") -> "application/dash+xml"
            url.contains(".ts") -> "video/mp2t"
            url.contains(".rtmp") -> null
            url.contains("rtsp://") -> null
            url.contains(".mp4") -> "video/mp4"
            url.contains(".mkv") -> "video/x-matroska"
            url.contains(".avi") -> "video/avi"
            else -> null
        }
    }

    fun buildMediaSource(uri: Uri, mimeType: String?): MediaSource {
        return when {
            mimeType == "application/x-mpegURL" || uri.toString().contains(".m3u8") -> {
                HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .setAllowChunklessPreparation(true)
                    .createMediaSource(MediaItem.fromUri(uri))
            }
            uri.toString().startsWith("rtsp://") -> {
                RtspMediaSource.Factory()
                    .createMediaSource(MediaItem.fromUri(uri))
            }
            else -> {
                DefaultMediaSourceFactory(DefaultExtractorsFactory())
                    .createMediaSource(MediaItem.fromUri(uri))
            }
        }
    }

    fun release() {
        player.stop()
        player.release()
    }
}
