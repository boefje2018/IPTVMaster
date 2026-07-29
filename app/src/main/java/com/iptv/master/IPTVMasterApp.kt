package com.iptv.master

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IPTVMasterApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
