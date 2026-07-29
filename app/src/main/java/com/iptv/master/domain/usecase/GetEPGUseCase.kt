package com.iptv.master.domain.usecase

import com.iptv.master.domain.model.EPGProgram
import com.iptv.master.domain.repository.EPGRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEPGUseCase @Inject constructor(
    private val repository: EPGRepository
) {
    operator fun invoke(channelId: String): Flow<List<EPGProgram>> {
        return repository.getProgramsForChannel(channelId)
    }
}
