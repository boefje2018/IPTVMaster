package com.iptv.master.domain.model

data class AppUpdate(
    val latestVersion: String,
    val downloadUrl: String,
    val changelog: String,
    val isMandatory: Boolean = false
)
