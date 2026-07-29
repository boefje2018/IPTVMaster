package com.iptv.master.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iptv.master.service.BackgroundAudioService

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PLAY = "com.iptv.master.notification.PLAY"
        const val ACTION_PAUSE = "com.iptv.master.notification.PAUSE"
        const val ACTION_CLOSE = "com.iptv.master.notification.CLOSE"
        const val EXTRA_STREAM_URL = "extra_stream_url"
        const val EXTRA_CHANNEL_NAME = "extra_channel_name"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val streamUrl = intent.getStringExtra(EXTRA_STREAM_URL) ?: return
        val channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""

        when (intent.action) {
            ACTION_PLAY -> {
                val serviceIntent = Intent(context, BackgroundAudioService::class.java).apply {
                    putExtra(BackgroundAudioService.EXTRA_STREAM_URL, streamUrl)
                    putExtra(BackgroundAudioService.EXTRA_CHANNEL_NAME, channelName)
                    action = BackgroundAudioService.ACTION_PLAY
                }
                context.startService(serviceIntent)
            }
            ACTION_PAUSE -> {
                val serviceIntent = Intent(context, BackgroundAudioService::class.java).apply {
                    action = BackgroundAudioService.ACTION_PAUSE
                }
                context.startService(serviceIntent)
            }
            ACTION_CLOSE -> {
                val serviceIntent = Intent(context, BackgroundAudioService::class.java).apply {
                    action = BackgroundAudioService.ACTION_STOP
                }
                context.startService(serviceIntent)
            }
        }
    }
}
