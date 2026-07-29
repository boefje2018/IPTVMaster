package com.iptv.master.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getString(key: String, default: String = ""): Flow<String>
    fun getBoolean(key: String, default: Boolean = false): Flow<Boolean>
    fun getInt(key: String, default: Int = 0): Flow<Int>
    suspend fun setString(key: String, value: String)
    suspend fun setBoolean(key: String, value: Boolean)
    suspend fun setInt(key: String, value: Int)
}
