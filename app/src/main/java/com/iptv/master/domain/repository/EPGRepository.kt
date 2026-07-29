package com.iptv.master.domain.repository

import com.iptv.master.domain.model.EPGProgram
import kotlinx.coroutines.flow.Flow

interface EPGRepository {
    fun getProgramsForChannel(channelId: String): Flow<List<EPGProgram>>
    fun getCurrentProgram(channelId: String): Flow<EPGProgram?>
    fun getNextProgram(channelId: String): Flow<EPGProgram?>
    suspend fun updateEPG(url: String): Result<Unit>
    suspend fun searchPrograms(query: String): List<EPGProgram>
}
