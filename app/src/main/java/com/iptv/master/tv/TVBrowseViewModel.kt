package com.iptv.master.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.repository.ChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVBrowseViewModel @Inject constructor(
    private val channelRepository: ChannelRepository
) : ViewModel() {

    private val _channelsByCategory = MutableLiveData<Map<String, List<Channel>>>()
    val channelsByCategory: LiveData<Map<String, List<Channel>>> = _channelsByCategory

    private val _allChannels = MutableLiveData<List<Channel>>()
    val allChannels: LiveData<List<Channel>> = _allChannels

    init {
        loadChannels()
    }

    private fun loadChannels() {
        viewModelScope.launch {
            channelRepository.getAllChannels().collect { channels ->
                _allChannels.value = channels
                _channelsByCategory.value = channels.groupBy { it.category.ifBlank { "General" } }
            }
        }
    }
}
