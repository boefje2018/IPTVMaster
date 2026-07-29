package com.iptv.master.data.repository

import com.iptv.master.data.remote.EPGParser
import com.iptv.master.data.remote.GitHubService
import com.iptv.master.domain.model.EPGProgram
import com.iptv.master.domain.repository.EPGRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EPGRepositoryImpl @Inject constructor(
    private val gitHubService: GitHubService
) : EPGRepository {

    private val programsMap = MutableStateFlow<Map<String, List<EPGProgram>>>(emptyMap())
    private val currentProgramMap = MutableStateFlow<Map<String, EPGProgram?>>(emptyMap())
    private val nextProgramMap = MutableStateFlow<Map<String, EPGProgram?>>(emptyMap())

    override fun getProgramsForChannel(channelId: String): Flow<List<EPGProgram>> {
        return programsMap.map { map ->
            map[channelId]?.sortedBy { it.startTime } ?: emptyList()
        }
    }

    override fun getCurrentProgram(channelId: String): Flow<EPGProgram?> {
        return programsMap.map { map ->
            val programs = map[channelId] ?: return@map null
            val now = System.currentTimeMillis()
            programs.find { it.startTime <= now && it.endTime > now }
        }
    }

    override fun getNextProgram(channelId: String): Flow<EPGProgram?> {
        return programsMap.map { map ->
            val programs = map[channelId] ?: return@map null
            val now = System.currentTimeMillis()
            programs.find { it.startTime > now }
        }
    }

    override suspend fun updateEPG(url: String): Result<Unit> {
        return try {
            val response = gitHubService.fetchRawContent(url)
            val xmlContent = response.string()
            val parsed = EPGParser.parse(xmlContent)
            programsMap.value = parsed

            val now = System.currentTimeMillis()
            val current = mutableMapOf<String, EPGProgram?>()
            val next = mutableMapOf<String, EPGProgram?>()
            for ((channelId, programs) in parsed) {
                val sorted = programs.sortedBy { it.startTime }
                current[channelId] = sorted.find { it.startTime <= now && it.endTime > now }
                next[channelId] = sorted.find { it.startTime > now }
            }
            currentProgramMap.value = current
            nextProgramMap.value = next

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPrograms(query: String): List<EPGProgram> {
        val q = query.lowercase()
        return programsMap.value.values.flatten().filter {
            it.title.lowercase().contains(q) ||
                (it.description?.lowercase()?.contains(q) == true)
        }
    }
}
