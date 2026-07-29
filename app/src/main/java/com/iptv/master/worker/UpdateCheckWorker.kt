package com.iptv.master.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iptv.master.R
import com.iptv.master.domain.usecase.CheckUpdateUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val checkUpdateUseCase: CheckUpdateUseCase
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val CHANNEL_ID = "update_check"
        private const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        return try {
            val result = checkUpdateUseCase()
            result.onSuccess { update ->
                showUpdateNotification(update.latestVersion, update.changelog, update.downloadUrl)
            }
            Result.success()
        } catch (e: Exception) {
            Result.success()
        }
    }

    private fun showUpdateNotification(version: String, changelog: String, downloadUrl: String) {
        createNotificationChannel()

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = android.net.Uri.parse(downloadUrl)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_update)
            .setContentTitle("Update Available v$version")
            .setContentText("A new version of IPTV Master is available")
            .setStyle(NotificationCompat.BigTextStyle().bigText(changelog.take(500)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            applicationContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Update Check",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for app updates"
        }
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
