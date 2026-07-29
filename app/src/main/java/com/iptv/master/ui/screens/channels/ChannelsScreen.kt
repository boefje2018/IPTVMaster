package com.iptv.master.ui.screens.channels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iptv.master.ui.components.CategoryRow
import com.iptv.master.ui.components.ChannelCard
import com.iptv.master.ui.components.ErrorView
import com.iptv.master.ui.components.LoadingIndicator
import com.iptv.master.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(
    navController: NavController,
    viewModel: ChannelListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isTablet = LocalConfiguration.current.screenWidthDp >= 600
    val columns = if (isTablet) 3 else 2

    when {
        uiState.isLoading && uiState.channels.isEmpty() -> {
            LoadingIndicator(message = "Loading channels...")
        }
        uiState.error != null -> {
            ErrorView(
                message = uiState.error!!,
                onRetry = { viewModel.refresh() }
            )
        }
        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ScrollableTabRow(
                    selectedTabIndex = ContentTab.entries.indexOf(uiState.selectedTab),
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 16.dp
                ) {
                    ContentTab.entries.forEach { tab ->
                        Tab(
                            selected = uiState.selectedTab == tab,
                            onClick = { viewModel.selectTab(tab) },
                            text = {
                                Text(
                                    text = tab.label,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    }
                }

                PullToRefreshBox(
                    isRefreshing = uiState.isLoading,
                    onRefresh = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 80.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item(span = { GridCells.Fixed(columns) }) {
                            CategoryRow(
                                categories = uiState.categories,
                                selectedCategory = uiState.selectedCategory,
                                onCategorySelected = { viewModel.selectCategory(it) },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(uiState.filteredChannels, key = { it.id }) { channel ->
                            ChannelCard(
                                channel = channel,
                                onClick = {
                                    navController.navigate(Screen.Player.createRoute(channel.id))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
