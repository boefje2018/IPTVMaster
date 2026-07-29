package com.iptv.master.domain.usecase

import com.iptv.master.domain.model.Channel
import com.iptv.master.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    operator fun invoke(category: String? = null): Flow<List<Channel>> {
        return if (category != null) repository.getChannelsByCategory(category)
        else repository.getAllChannels()
    }
}
