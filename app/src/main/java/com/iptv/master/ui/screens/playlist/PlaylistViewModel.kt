package com.iptv.master.ui.screens.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Playlist
import com.iptv.master.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaylistUiState(
    val playlists: List<Playlist> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val syncingIds: Set<String> = emptySet()
)

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistRepository.getAllPlaylists().collect { playlists ->
                _uiState.update {
                    it.copy(
                        playlists = playlists,
                        isLoading = false,
                        isEmpty = playlists.isEmpty()
                    )
                }
            }
        }
    }

    fun deletePlaylist(id: String) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(id)
        }
    }

    fun syncPlaylist(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(syncingIds = it.syncingIds + id) }
            try {
                playlistRepository.syncPlaylist(id)
            } catch (_: Exception) { }
            _uiState.update { it.copy(syncingIds = it.syncingIds - id) }
        }
    }

    fun addPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.addPlaylist(playlist)
        }
    }
}
