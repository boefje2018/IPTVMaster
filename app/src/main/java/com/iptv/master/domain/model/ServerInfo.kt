package com.iptv.master.domain.model

data class ServerInfo(
    val url: String,
    val port: Int = 80,
    val isActive: Boolean = false,
    val version: String? = null,
    val timezone: String? = null
)
