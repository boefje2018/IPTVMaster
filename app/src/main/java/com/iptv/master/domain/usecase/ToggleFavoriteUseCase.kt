package com.iptv.master.domain.usecase

import com.iptv.master.domain.repository.ChannelRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    suspend operator fun invoke(channelId: String) {
        repository.toggleFavorite(channelId)
    }
}
