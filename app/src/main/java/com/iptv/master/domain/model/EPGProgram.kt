package com.iptv.master.domain.model

data class EPGProgram(
    val id: String,
    val channelId: String,
    val title: String,
    val description: String? = null,
    val startTime: Long,
    val endTime: Long,
    val category: String? = null,
    val icon: String? = null,
    val rating: String? = null,
    val isLive: Boolean = false
)
