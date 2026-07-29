package com.iptv.master.ui.screens.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iptv.master.domain.model.Playlist
import com.iptv.master.domain.model.PlaylistType
import com.iptv.master.domain.usecase.AddPlaylistUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddPlaylistUiState(
    val name: String = "",
    val url: String = "",
    val serverUrl: String = "",
    val username: String = "",
    val password: String = "",
    val macAddress: String = "",
    val selectedTabIndex: Int = 0,
    val isSaving: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddPlaylistViewModel @Inject constructor(
    private val addPlaylistUseCase: AddPlaylistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddPlaylistUiState())
    val uiState: StateFlow<AddPlaylistUiState> = _uiState.asStateFlow()

    fun updateName(name: String) { _uiState.update { it.copy(name = name) } }
    fun updateUrl(url: String) { _uiState.update { it.copy(url = url) } }
    fun updateServerUrl(serverUrl: String) { _uiState.update { it.copy(serverUrl = serverUrl) } }
    fun updateUsername(username: String) { _uiState.update { it.copy(username = username) } }
    fun updatePassword(password: String) { _uiState.update { it.copy(password = password) } }
    fun updateMacAddress(mac: String) { _uiState.update { it.copy(macAddress = mac) } }
    fun selectTab(index: Int) { _uiState.update { it.copy(selectedTabIndex = index) } }

    fun save(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val state = _uiState.value
            if (state.name.isBlank()) {
                _uiState.update { it.copy(isSaving = false, error = "Please enter a playlist name") }
                return@launch
            }

            val type = when (state.selectedTabIndex) {
                0 -> PlaylistType.M3U_URL
                1 -> PlaylistType.XTREAM_CODES
                else -> PlaylistType.MAC_PORTAL
            }

            val playlist = Playlist(
                name = state.name.trim(),
                url = if (type == PlaylistType.M3U_URL) state.url.trim() else null,
                playlistType = type,
                serverUrl = if (type != PlaylistType.M3U_URL) state.serverUrl.trim().ifBlank { null } else null,
                username = if (type == PlaylistType.XTREAM_CODES) state.username.trim().ifBlank { null } else null,
                password = if (type == PlaylistType.XTREAM_CODES) state.password.trim().ifBlank { null } else null,
                macAddress = if (type == PlaylistType.MAC_PORTAL) state.macAddress.trim().ifBlank { null } else null
            )

            val result = addPlaylistUseCase(playlist)
            result.onSuccess { onSuccess() }
                .onFailure { e -> _uiState.update { it.copy(isSaving = false, error = e.message) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaylistScreen(
    navController: NavController,
    viewModel: AddPlaylistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("M3U URL", "Xtream Codes", "MAC/Portal")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Playlist") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Playlist Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTabIndex == index,
                        onClick = { viewModel.selectTab(index) },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState.selectedTabIndex) {
                0 -> M3UForm(uiState.url) { viewModel.updateUrl(it) }
                1 -> XtreamForm(
                    serverUrl = uiState.serverUrl,
                    username = uiState.username,
                    password = uiState.password,
                    onServerUrlChange = { viewModel.updateServerUrl(it) },
                    onUsernameChange = { viewModel.updateUsername(it) },
                    onPasswordChange = { viewModel.updatePassword(it) }
                )
                2 -> MACForm(
                    serverUrl = uiState.serverUrl,
                    macAddress = uiState.macAddress,
                    onServerUrlChange = { viewModel.updateServerUrl(it) },
                    onMacChange = { viewModel.updateMacAddress(it) }
                )
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = uiState.error!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.save { navController.popBackStack() } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (uiState.isSaving) "Saving..." else "Save Playlist")
            }
        }
    }
}

@Composable
private fun M3UForm(
    url: String,
    onUrlChange: (String) -> Unit
) {
    OutlinedTextField(
        value = url,
        onValueChange = onUrlChange,
        label = { Text("M3U URL") },
        placeholder = { Text("https://example.com/playlist.m3u") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun XtreamForm(
    serverUrl: String,
    username: String,
    password: String,
    onServerUrlChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = serverUrl,
            onValueChange = onServerUrlChange,
            label = { Text("Server URL") },
            placeholder = { Text("http://example.com:8080") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun MACForm(
    serverUrl: String,
    macAddress: String,
    onServerUrlChange: (String) -> Unit,
    onMacChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = serverUrl,
            onValueChange = onServerUrlChange,
            label = { Text("Server URL") },
            placeholder = { Text("http://example.com:8080") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = macAddress,
            onValueChange = onMacChange,
            label = { Text("MAC Address") },
            placeholder = { Text("00:1A:2B:3C:4D:5E") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
