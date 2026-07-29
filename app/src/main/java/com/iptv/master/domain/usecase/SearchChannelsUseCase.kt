package com.iptv.master.domain.usecase

import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    operator fun invoke(query: String): Flow<List<Channel>> {
        return repository.searchChannels(query)
    }
}
