package com.iptv.master.domain.model

enum class ContentType {
    LIVE, MOVIE, SERIES, UNKNOWN
}

data class Channel(
    val id: String,
    val name: String,
    val logoUrl: String? = null,
    val streamUrl: String,
    val category: String = "General",
    val country: String? = null,
    val language: String? = null,
    val epgChannelId: String? = null,
    val isFavorite: Boolean = false,
    val isAdult: Boolean = false,
    val groupTitle: String = "General",
    val tvgId: String? = null,
    val tvgName: String? = null,
    val tvgLogo: String? = null,
    val contentType: ContentType = ContentType.UNKNOWN
)
