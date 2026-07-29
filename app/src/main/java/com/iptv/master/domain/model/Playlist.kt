package com.iptv.master.domain.model

data class Playlist(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val url: String? = null,
    val playlistType: PlaylistType,
    val serverUrl: String? = null,
    val username: String? = null,
    val password: String? = null,
    val macAddress: String? = null,
    val isActive: Boolean = true,
    val lastSynced: Long? = null,
    val channelCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PlaylistType {
    M3U_URL, XTREAM_CODES, MAC_PORTAL, LOCAL_FILE, GITHUB
}
