package com.iptv.master.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iptv.master.domain.repository.EPGRepository
import com.iptv.master.domain.repository.SettingsRepository
import com.iptv.master.util.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class EPGSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val epgRepository: EPGRepository,
    private val settingsRepository: SettingsRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val url = settingsRepository.getString(Constants.KEY_DEFAULT_EPG_URL).let {
                var result = ""
                it.collect { value -> result = value }
                result
            }
            if (url.isBlank()) return Result.success()
            val result = epgRepository.updateEPG(url)
            if (result.isSuccess) Result.success() else Result.retry()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
