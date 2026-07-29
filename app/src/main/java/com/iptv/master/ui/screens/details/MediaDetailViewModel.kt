package com.iptv.master.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.MediaItem
import com.iptv.master.domain.model.MediaType
import com.iptv.master.domain.repository.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MediaDetailState(
    val item: MediaItem? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class MediaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tmdbRepository: TmdbRepository
) : ViewModel() {

    private val mediaId: Long = savedStateHandle["mediaId"] ?: 0L
    private val mediaType: String = savedStateHandle["mediaType"] ?: "movie"

    private val _state = MutableStateFlow(MediaDetailState())
    val state: StateFlow<MediaDetailState> = _state.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _state.value = MediaDetailState(isLoading = true)
            try {
                val item = if (mediaType == "tv") {
                    tmdbRepository.getTvDetail(mediaId)
                } else {
                    tmdbRepository.getMovieDetail(mediaId)
                }
                _state.value = MediaDetailState(item = item, isLoading = false)
            } catch (e: Exception) {
                _state.value = MediaDetailState(
                    isLoading = false,
                    error = e.message ?: "Detay yüklenemedi"
                )
            }
        }
    }
}
