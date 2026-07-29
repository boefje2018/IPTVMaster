package com.iptv.master.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.model.ChannelCategory
import com.iptv.master.domain.model.ContentType
import com.iptv.master.domain.usecase.GetChannelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val featuredChannels: List<Channel> = emptyList(),
    val recentlyWatched: List<Channel> = emptyList(),
    val liveChannels: List<Channel> = emptyList(),
    val movieChannels: List<Channel> = emptyList(),
    val seriesChannels: List<Channel> = emptyList(),
    val categories: List<ChannelCategory> = emptyList(),
    val liveCount: Int = 0,
    val movieCount: Int = 0,
    val seriesCount: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getChannelsUseCase: GetChannelsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun refresh() {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getChannelsUseCase().collect { channels ->
                val cats = channels.groupBy { it.category }.map { (name, list) ->
                    ChannelCategory(name = name, channelCount = list.size)
                }
                val live = channels.filter { it.contentType == ContentType.LIVE || it.contentType == ContentType.UNKNOWN }
                val movies = channels.filter { it.contentType == ContentType.MOVIE }
                val series = channels.filter { it.contentType == ContentType.SERIES }
                _uiState.update {
                    it.copy(
                        featuredChannels = channels.shuffled().take(12),
                        recentlyWatched = channels.take(6),
                        liveChannels = live.take(12),
                        movieChannels = movies.take(12),
                        seriesChannels = series.take(12),
                        categories = cats,
                        liveCount = live.size,
                        movieCount = movies.size,
                        seriesCount = series.size,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
}
