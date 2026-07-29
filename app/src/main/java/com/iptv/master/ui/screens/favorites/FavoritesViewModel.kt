package com.iptv.master.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.repository.ChannelRepository
import com.iptv.master.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val channels: List<Channel> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val channelRepository: ChannelRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            channelRepository.getFavoriteChannels().collect { channels ->
                _uiState.update {
                    it.copy(channels = channels, isLoading = false, isEmpty = channels.isEmpty())
                }
            }
        }
    }

    fun removeFavorite(channelId: String) {
        viewModelScope.launch { toggleFavoriteUseCase(channelId) }
    }
}
