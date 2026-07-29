package com.iptv.master.player

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational

object PiPManager {

    private var isInPipMode = false

    fun isPipSupported(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            } else {
                true
            }
        } else {
            false
        }
    }

    fun enterPipMode(
        activity: Activity,
        aspectRatioWidth: Int = 16,
        aspectRatioHeight: Int = 9,
        autoEnterEnabled: Boolean = true
    ): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return false
        if (!isPipSupported(activity)) return false

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(aspectRatioWidth, aspectRatioHeight))
                    .setAutoEnterEnabled(autoEnterEnabled)
                    .build()
                activity.enterPictureInPictureMode(params)
            } else {
                @Suppress("DEPRECATION")
                activity.enterPictureInPictureMode()
            }
            isInPipMode = true
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updatePipParams(
        activity: Activity,
        aspectRatioWidth: Int = 16,
        aspectRatioHeight: Int = 9,
        autoEnterEnabled: Boolean = true
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(aspectRatioWidth, aspectRatioHeight))
                    .setAutoEnterEnabled(autoEnterEnabled)
                    .build()
                activity.setPictureInPictureParams(params)
            } catch (_: Exception) { }
        }
    }

    fun onPipModeChanged(isInPictureInPictureMode: Boolean) {
        isInPipMode = isInPictureInPictureMode
    }

    fun isCurrentlyInPip(): Boolean = isInPipMode

    fun exitPipMode(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInPipMode) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val params = PictureInPictureParams.Builder()
                        .setAspectRatio(Rational(16, 9))
                        .build()
                    activity.enterPictureInPictureMode(params)
                }
            } catch (_: Exception) { }
        }
    }
}
