package com.iptv.master.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes as Media3AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.iptv.master.MainActivity
import com.iptv.master.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundAudioService : Service() {

    companion object {
        private const val CHANNEL_ID = "background_audio"
        private const val NOTIFICATION_ID = 2001
        private const val ACTION_PLAY = "com.iptv.master.action.PLAY"
        private const val ACTION_PAUSE = "com.iptv.master.action.PAUSE"
        private const val ACTION_STOP = "com.iptv.master.action.STOP"
        const val EXTRA_STREAM_URL = "stream_url"
        const val EXTRA_CHANNEL_NAME = "channel_name"
    }

    private var player: ExoPlayer? = null
    private var mediaSession: MediaSessionCompat? = null
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private var streamUrl: String? = null
    private var channelName: String? = null

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        createNotificationChannel()
        setupMediaSession()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                streamUrl = intent.getStringExtra(EXTRA_STREAM_URL)
                channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME)
                startPlayback()
            }
            ACTION_PAUSE -> pausePlayback()
            ACTION_STOP -> stopPlayback()
            else -> {
                streamUrl = intent?.getStringExtra(EXTRA_STREAM_URL)
                channelName = intent?.getStringExtra(EXTRA_CHANNEL_NAME)
                startPlayback()
            }
        }
        return START_STICKY
    }

    private fun startPlayback() {
        if (streamUrl.isNullOrBlank()) return
        requestAudioFocus()
        startForeground(NOTIFICATION_ID, buildNotification(isPlaying = true))

        if (player == null) {
            player = ExoPlayer.Builder(this)
                .setAudioAttributes(
                    Media3AudioAttributes.Builder()
                        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                        .setUsage(C.USAGE_MEDIA)
                        .build(),
                    true
                )
                .build()
                .apply {
                    playWhenReady = true
                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            when (playbackState) {
                                Player.STATE_READY -> {
                                    mediaSession?.setPlaybackState(
                                        PlaybackStateCompat.Builder()
                                            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f)
                                            .build()
                                    )
                                }
                                Player.STATE_ENDED -> stopSelf()
                            }
                        }
                    })
                }
        }

        player?.setMediaItem(MediaItem.fromUri(streamUrl!!))
        player?.prepare()
    }

    private fun pausePlayback() {
        player?.pause()
        mediaSession?.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1f)
                .build()
        )
        val notification = buildNotification(isPlaying = false)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun stopPlayback() {
        player?.stop()
        player?.release()
        player = null
        abandonAudioFocus()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS -> pausePlayback()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pausePlayback()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> player?.volume = 0.3f
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            player?.play()
                            player?.volume = 1.0f
                        }
                    }
                }
                .build()
            audioFocusRequest = focusRequest
            audioManager?.requestAudioFocus(focusRequest)
        } else {
            @Suppress("DEPRECATION")
            audioManager?.requestAudioFocus(
                { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS -> pausePlayback()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pausePlayback()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> player?.volume = 0.3f
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            player?.play()
                            player?.volume = 1.0f
                        }
                    }
                },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    private fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager?.abandonAudioFocus(null)
        }
    }

    private fun setupMediaSession() {
        mediaSession = MediaSessionCompat(this, "BackgroundAudioService")
        mediaSession?.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() { startPlayback() }
            override fun onPause() { pausePlayback() }
            override fun onStop() { stopPlayback() }
        })
        mediaSession?.isActive = true
        mediaSession?.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, channelName ?: "IPTV Master")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Live Stream")
                .build()
        )
        mediaSession?.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f)
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_STOP
                )
                .build()
        )
    }

    private fun buildNotification(isPlaying: Boolean): Notification {
        val playPauseIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, BackgroundAudioService::class.java).apply {
                action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
                putExtra(EXTRA_STREAM_URL, streamUrl)
                putExtra(EXTRA_CHANNEL_NAME, channelName)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, BackgroundAudioService::class.java).apply {
                action = ACTION_STOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val openAppIntent = PendingIntent.getActivity(
            this,
            2,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_audio)
            .setContentTitle(channelName ?: "IPTV Master")
            .setContentText(if (isPlaying) "Playing..." else "Paused")
            .setOngoing(isPlaying)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(openAppIntent)
            .addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                if (isPlaying) "Pause" else "Play",
                playPauseIntent
            )
            .addAction(R.drawable.ic_close, "Stop", stopIntent)
            .setStyle(NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession?.sessionToken)
                .setShowActionsInCompactView(0, 1))
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Background Audio",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Background audio playback notification"
            setShowBadge(false)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        player?.release()
        player = null
        mediaSession?.isActive = false
        mediaSession?.release()
        mediaSession = null
        abandonAudioFocus()
        super.onDestroy()
    }
}
