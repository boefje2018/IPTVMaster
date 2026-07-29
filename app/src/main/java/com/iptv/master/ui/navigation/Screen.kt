package com.iptv.master.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val titleRes: String, val icon: ImageVector? = null) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Discover : Screen("discover", "Keşfet", Icons.Default.VideoLibrary)
    data object Channels : Screen("channels", "Channels", Icons.Default.Tv)
    data object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    data object Search : Screen("search", "Search")
    data object Player : Screen("player/{channelId}", "Player") {
        fun createRoute(channelId: String) = "player/$channelId"
    }
    data object PlaylistManager : Screen("playlist_manager", "Playlists")
    data object AddPlaylist : Screen("add_playlist", "Add Playlist")
    data object About : Screen("about", "About")
    data object Donation : Screen("donation", "Donation")
    data object ProgramDetail : Screen("program_detail/{programId}", "Program Detail") {
        fun createRoute(programId: String) = "program_detail/$programId"
    }
    data object MediaDetail : Screen("media_detail/{mediaType}/{mediaId}", "Media Detail") {
        fun createRoute(mediaType: String, mediaId: Long) = "media_detail/$mediaType/$mediaId"
    }

    companion object {
        val bottomNavItems = listOf(Home, Discover, Channels, Favorites, Settings)
    }
}
