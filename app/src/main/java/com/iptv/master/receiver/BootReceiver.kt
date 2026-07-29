package com.iptv.master.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.iptv.master.util.Constants
import com.iptv.master.worker.EPGSyncWorker
import com.iptv.master.worker.PlaylistSyncWorker
import com.iptv.master.worker.UpdateCheckWorker
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            scheduleWorkers(context)
        }
    }

    private fun scheduleWorkers(context: Context) {
        val workManager = WorkManager.getInstance(context)

        val playlistSyncRequest = PeriodicWorkRequestBuilder<PlaylistSyncWorker>(
            Constants.PLAYLIST_SYNC_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setInitialDelay(1, TimeUnit.MINUTES)
            .addTag("playlist_sync")
            .build()

        val epgSyncRequest = PeriodicWorkRequestBuilder<EPGSyncWorker>(
            Constants.EPG_SYNC_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setInitialDelay(2, TimeUnit.MINUTES)
            .addTag("epg_sync")
            .build()

        val updateCheckRequest = PeriodicWorkRequestBuilder<UpdateCheckWorker>(
            Constants.UPDATE_CHECK_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setInitialDelay(3, TimeUnit.MINUTES)
            .addTag("update_check")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "playlist_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            playlistSyncRequest
        )

        workManager.enqueueUniquePeriodicWork(
            "epg_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            epgSyncRequest
        )

        workManager.enqueueUniquePeriodicWork(
            "update_check",
            ExistingPeriodicWorkPolicy.KEEP,
            updateCheckRequest
        )
    }
}
