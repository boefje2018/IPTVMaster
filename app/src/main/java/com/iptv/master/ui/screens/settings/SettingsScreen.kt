package com.iptv.master.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iptv.master.ui.components.LoadingIndicator
import com.iptv.master.ui.navigation.Screen

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        LoadingIndicator(message = "Loading settings...")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SettingsGroupHeader(title = "General")
            }

            item {
                DropdownSetting(
                    icon = Icons.Default.Language,
                    title = "Language",
                    value = uiState.language.uppercase(),
                    options = listOf("EN", "TR", "DE", "FR", "ES"),
                    onOptionSelected = { viewModel.setLanguage(it.lowercase()) }
                )
            }

            item {
                DropdownSetting(
                    icon = Icons.Default.SettingsBrightness,
                    title = "Theme",
                    value = uiState.theme.replaceFirstChar { it.uppercase() },
                    options = listOf("System", "Light", "Dark"),
                    onOptionSelected = { viewModel.setTheme(it.lowercase()) }
                )
            }

            item {
                SwitchSetting(
                    icon = Icons.Default.Palette,
                    title = "Dynamic Color",
                    checked = uiState.dynamicColor,
                    onCheckedChange = { viewModel.setDynamicColor(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsGroupHeader(title = "Player")
            }

            item {
                SliderSetting(
                    title = "Buffer Size",
                    value = uiState.bufferSize.toFloat(),
                    valueText = "${uiState.bufferSize}s",
                    onValueChange = { viewModel.setBufferSize(it.toInt()) },
                    valueRange = 1f..30f
                )
            }

            item {
                SwitchSetting(
                    icon = Icons.Default.PlayArrow,
                    title = "Hardware Decoding",
                    checked = uiState.hardwareDecoding,
                    onCheckedChange = { viewModel.setHardwareDecoding(it) }
                )
            }

            item {
                SwitchSetting(
                    icon = Icons.Default.PlayArrow,
                    title = "Auto Quality",
                    checked = uiState.autoQuality,
                    onCheckedChange = { viewModel.setAutoQuality(it) }
                )
            }

            item {
                SwitchSetting(
                    icon = Icons.Default.PlayArrow,
                    title = "Background Play",
                    checked = uiState.backgroundPlay,
                    onCheckedChange = { viewModel.setBackgroundPlay(it) }
                )
            }

            item {
                SwitchSetting(
                    icon = Icons.Default.PlayArrow,
                    title = "Picture-in-Picture",
                    checked = uiState.pictureInPicture,
                    onCheckedChange = { viewModel.setPictureInPicture(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsGroupHeader(title = "Playlists")
            }

            item {
                NavigateSetting(
                    icon = Icons.Default.PlaylistPlay,
                    title = "Manage Playlists",
                    onClick = { navController.navigate(Screen.PlaylistManager.route) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsGroupHeader(title = "Parental Control")
            }

            item {
                NavigateSetting(
                    icon = Icons.Default.Lock,
                    title = "Parental Control Settings",
                    onClick = { navController.navigate(Screen.About.route) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsGroupHeader(title = "Support")
            }

            item {
                NavigateSetting(
                    icon = Icons.Default.Coffee,
                    title = "Donation",
                    onClick = { navController.navigate(Screen.Donation.route) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsGroupHeader(title = "About")
            }

            item {
                NavigateSetting(
                    icon = Icons.Default.Info,
                    title = "About IPTV Master",
                    onClick = { navController.navigate(Screen.About.route) }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SwitchSetting(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSetting(
    icon: ImageVector,
    title: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SliderSetting(
    title: String,
    value: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = valueText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun NavigateSetting(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 12.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
