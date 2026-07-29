package com.iptv.master.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.iptv.master.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : SettingsRepository {

    override fun getString(key: String, default: String): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(key)] ?: default
        }
    }

    override fun getBoolean(key: String, default: Boolean): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[booleanPreferencesKey(key)] ?: default
        }
    }

    override fun getInt(key: String, default: Int): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[intPreferencesKey(key)] ?: default
        }
    }

    override suspend fun setString(key: String, value: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun setBoolean(key: String, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(key)] = value
        }
    }

    override suspend fun setInt(key: String, value: Int) {
        context.dataStore.edit { prefs ->
            prefs[intPreferencesKey(key)] = value
        }
    }
}
