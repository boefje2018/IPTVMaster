package com.iptv.master.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.model.MediaItem
import com.iptv.master.domain.repository.TmdbRepository
import com.iptv.master.domain.usecase.SearchChannelsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val channels: List<Channel> = emptyList(),
    val tmdbResults: List<MediaItem> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchChannelsUseCase: SearchChannelsUseCase,
    private val tmdbRepository: TmdbRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        if (query.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(300)
                _uiState.update { it.copy(isSearching = true, hasSearched = true) }

                val channelDeferred = async {
                    searchChannelsUseCase(query).first()
                }
                val tmdbDeferred = async {
                    tmdbRepository.search(query)
                }

                val channels = channelDeferred.await()
                val tmdb = tmdbDeferred.await()

                _uiState.update {
                    it.copy(
                        channels = channels,
                        tmdbResults = tmdb,
                        suggestions = (channels.map { c -> c.name } + tmdb.map { it.title }).take(5),
                        isSearching = false
                    )
                }
            }
        } else {
            _uiState.update { it.copy(channels = emptyList(), tmdbResults = emptyList(), suggestions = emptyList(), isSearching = false) }
        }
    }

    fun clearSearch() {
        _uiState.update { SearchUiState() }
    }
}
