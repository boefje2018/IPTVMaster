package com.iptv.master.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.MediaCategory
import com.iptv.master.domain.model.MediaItem
import com.iptv.master.domain.repository.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscoverUiState(
    val categories: Map<MediaCategory, List<MediaItem>> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val categories = listOf(
        MediaCategory.TRENDING_MOVIES,
        MediaCategory.TRENDING_TV,
        MediaCategory.POPULAR_MOVIES,
        MediaCategory.POPULAR_TV,
        MediaCategory.NOW_PLAYING,
        MediaCategory.TOP_RATED_TV,
        MediaCategory.UPCOMING,
        MediaCategory.ON_THE_AIR
    )

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = mutableMapOf<MediaCategory, List<MediaItem>>()
                for (category in categories) {
                    results[category] = tmdbRepository.getMediaByCategory(category)
                }
                _uiState.value = _uiState.value.copy(
                    categories = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Veriler yüklenirken hata oluştu"
                )
            }
        }
    }
}
