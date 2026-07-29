package com.iptv.master.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.repository.ChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVSearchViewModel @Inject constructor(
    private val channelRepository: ChannelRepository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Channel>>()
    val searchResults: LiveData<List<Channel>> = _searchResults

    private var searchJob: Job? = null

    fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            channelRepository.searchChannels(query).collect { results ->
                _searchResults.value = results
            }
        }
    }
}
