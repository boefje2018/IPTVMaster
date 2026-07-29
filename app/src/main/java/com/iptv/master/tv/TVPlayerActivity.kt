package com.iptv.master.tv

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackGlueHost
import androidx.leanback.widget.PlayerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.iptv.master.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TVPlayerActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PlayerViewModel
    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var playerView: PlayerView? = null
    private var isInfoVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            )

        viewModel = ViewModelProvider(this, viewModelFactory)[PlayerViewModel::class.java]

        val channelId = intent.getStringExtra("channelId") ?: return finish()

        playerView = PlayerView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            useController = true
            setBackgroundColor(android.graphics.Color.BLACK)
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }
        setContentView(playerView)

        player = ExoPlayer.Builder(this)
            .build()
            .apply {
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_ONE
            }

        playerView?.player = player

        mediaSession = MediaSession.Builder(this, player!!).build()

        viewModel.loadChannel(channelId)
        viewModel.uiState.observe(this) { state ->
            state.channel?.let { channel ->
                player?.let {
                    val mediaItem = androidx.media3.common.MediaItem.fromUri(channel.streamUrl)
                    it.setMediaItem(mediaItem)
                    it.prepare()
                    it.play()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                viewModel.nextChannel()
                true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                viewModel.previousChannel()
                true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                isInfoVisible = !isInfoVisible
                updateInfoOverlay()
                true
            }
            KeyEvent.KEYCODE_BACK -> {
                finish()
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player?.stop()
            release()
            mediaSession?.release()
        }
        player?.release()
        player = null
        super.onDestroy()
    }

    private fun updateInfoOverlay() {
        playerView?.useController = isInfoVisible
    }
}
