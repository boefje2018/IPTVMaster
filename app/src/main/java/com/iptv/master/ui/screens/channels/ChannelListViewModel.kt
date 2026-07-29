package com.iptv.master.ui.screens.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.model.ContentType
import com.iptv.master.domain.usecase.GetChannelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ContentTab(val label: String) {
    ALL("All"), LIVE("Live TV"), MOVIES("Movies"), SERIES("Series")
}

data class ChannelListUiState(
    val channels: List<Channel> = emptyList(),
    val filteredChannels: List<Channel> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val selectedTab: ContentTab = ContentTab.ALL,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChannelListUiState())
    val uiState: StateFlow<ChannelListUiState> = _uiState.asStateFlow()

    init { loadChannels() }

    fun refresh() { loadChannels() }

    private fun loadChannels() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getChannelsUseCase(_uiState.value.selectedCategory).collect { channels ->
                val cats = channels.map { it.category }.distinct().sorted()
                val filtered = filterChannels(channels, _uiState.value.selectedTab)
                _uiState.update {
                    it.copy(channels = channels, filteredChannels = filtered, categories = cats, isLoading = false)
                }
            }
        }
    }

    fun selectCategory(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadChannels()
    }

    fun selectTab(tab: ContentTab) {
        _uiState.update { state ->
            val filtered = filterChannels(state.channels, tab)
            state.copy(selectedTab = tab, filteredChannels = filtered)
        }
    }

    private fun filterChannels(channels: List<Channel>, tab: ContentTab): List<Channel> {
        return when (tab) {
            ContentTab.ALL -> channels
            ContentTab.LIVE -> channels.filter { it.contentType == ContentType.LIVE || it.contentType == ContentType.UNKNOWN }
            ContentTab.MOVIES -> channels.filter { it.contentType == ContentType.MOVIE }
            ContentTab.SERIES -> channels.filter { it.contentType == ContentType.SERIES }
        }
    }
}
