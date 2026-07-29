package com.iptv.master.ui.screens.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.model.EPGProgram
import com.iptv.master.domain.usecase.GetChannelsUseCase
import com.iptv.master.domain.usecase.GetEPGUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EPGUiState(
    val channels: List<Channel> = emptyList(),
    val selectedChannel: Channel? = null,
    val programs: Map<String, List<EPGProgram>> = emptyMap(),
    val currentTime: Long = System.currentTimeMillis(),
    val isLoading: Boolean = true
)

@HiltViewModel
class EPGViewModel @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getEPGUseCase: GetEPGUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EPGUiState())
    val uiState: StateFlow<EPGUiState> = _uiState.asStateFlow()

    init {
        loadEPG()
        startTimeUpdater()
    }

    private fun loadEPG() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getChannelsUseCase().collect { channels ->
                _uiState.update { it.copy(channels = channels.take(50), isLoading = false) }
                channels.take(50).forEach { channel ->
                    loadProgramsForChannel(channel.id, channel.name)
                }
            }
        }
    }

    private fun loadProgramsForChannel(channelId: String, channelName: String) {
        viewModelScope.launch {
            getEPGUseCase(channelId).collect { programs ->
                val updatedPrograms = _uiState.value.programs.toMutableMap()
                updatedPrograms[channelName] = programs
                _uiState.update { it.copy(programs = updatedPrograms) }
            }
        }
    }

    fun selectChannel(channelId: String) {
        val channel = _uiState.value.channels.find { it.id == channelId }
        _uiState.update { it.copy(selectedChannel = channel) }
    }

    private fun startTimeUpdater() {
        viewModelScope.launch {
            while (true) {
                _uiState.update { it.copy(currentTime = System.currentTimeMillis()) }
                delay(30_000)
            }
        }
    }
}
