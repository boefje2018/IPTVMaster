package com.iptv.master.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.model.EPGProgram
import com.iptv.master.domain.repository.ChannelRepository
import com.iptv.master.domain.usecase.GetEPGUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val channel: Channel? = null,
    val currentProgram: EPGProgram? = null,
    val nextProgram: EPGProgram? = null,
    val channels: List<Channel> = emptyList(),
    val currentIndex: Int = 0,
    val isPlaying: Boolean = false,
    val isLocked: Boolean = false,
    val showControls: Boolean = true
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val channelRepository: ChannelRepository,
    private val getEPGUseCase: GetEPGUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadChannel(channelId: String) {
        viewModelScope.launch {
            channelRepository.getAllChannels().collect { allChannels ->
                val index = allChannels.indexOfFirst { it.id == channelId }
                if (index >= 0) {
                    _uiState.update { it.copy(
                        channel = allChannels[index],
                        channels = allChannels,
                        currentIndex = index
                    )}
                    loadEPG(channelId)
                    channelRepository.addToHistory(channelId)
                }
            }
        }
    }

    private fun loadEPG(channelId: String) {
        viewModelScope.launch {
            getEPGUseCase(channelId).collect { programs ->
                val now = System.currentTimeMillis()
                _uiState.update { it.copy(
                    currentProgram = programs.find { now in it.startTime..it.endTime },
                    nextProgram = programs.find { it.startTime > now }
                )}
            }
        }
    }

    fun nextChannel() {
        val channels = _uiState.value.channels
        val newIndex = (_uiState.value.currentIndex + 1).coerceAtMost(channels.size - 1)
        channels.getOrNull(newIndex)?.let { loadChannel(it.id) }
    }

    fun previousChannel() {
        val newIndex = (_uiState.value.currentIndex - 1).coerceAtLeast(0)
        _uiState.value.channels.getOrNull(newIndex)?.let { loadChannel(it.id) }
    }

    fun toggleLock() { _uiState.update { it.copy(isLocked = !it.isLocked) } }
    fun toggleControls() { _uiState.update { it.copy(showControls = !it.showControls) } }
}
