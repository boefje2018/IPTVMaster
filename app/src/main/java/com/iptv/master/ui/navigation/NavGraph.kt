package com.iptv.master.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.iptv.master.ui.screens.channels.ChannelsScreen
import com.iptv.master.ui.screens.details.MediaDetailScreen
import com.iptv.master.ui.screens.discover.DiscoverScreen
import com.iptv.master.ui.screens.donation.DonationScreen
import com.iptv.master.ui.screens.favorites.FavoritesScreen
import com.iptv.master.ui.screens.home.HomeScreen
import com.iptv.master.ui.screens.playlist.AddPlaylistScreen
import com.iptv.master.ui.screens.playlist.PlaylistManagerScreen
import com.iptv.master.ui.screens.search.SearchScreen
import com.iptv.master.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Discover.route) {
            DiscoverScreen(
                onMediaClick = { item ->
                    val type = if (item.mediaType.name == "TV") "tv" else "movie"
                    navController.navigate(Screen.MediaDetail.createRoute(type, item.id))
                }
            )
        }

        composable(Screen.Channels.route) {
            ChannelsScreen(navController = navController)
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }

        composable(
            route = Screen.Player.route,
            arguments = listOf(navArgument("channelId") { type = NavType.StringType })
        ) { backStackEntry ->
            val channelId = backStackEntry.arguments?.getString("channelId") ?: return@composable
            com.iptv.master.ui.screens.channels.PlayerScreen(
                channelId = channelId,
                navController = navController
            )
        }

        composable(Screen.PlaylistManager.route) {
            PlaylistManagerScreen(navController = navController)
        }

        composable(Screen.AddPlaylist.route) {
            AddPlaylistScreen(navController = navController)
        }

        composable(Screen.About.route) {
            com.iptv.master.ui.screens.settings.AboutScreen(navController = navController)
        }

        composable(Screen.Donation.route) {
            DonationScreen(navController = navController)
        }

        composable(
            route = Screen.MediaDetail.route,
            arguments = listOf(
                navArgument("mediaType") { type = NavType.StringType },
                navArgument("mediaId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            MediaDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
