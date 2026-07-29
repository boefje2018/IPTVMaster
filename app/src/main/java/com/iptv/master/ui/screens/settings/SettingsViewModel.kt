package com.iptv.master.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val theme: String = "system",
    val language: String = "en",
    val dynamicColor: Boolean = true,
    val bufferSize: Int = 5,
    val hardwareDecoding: Boolean = true,
    val autoQuality: Boolean = true,
    val backgroundPlay: Boolean = false,
    val pictureInPicture: Boolean = true,
    val parentalControlEnabled: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.getString("theme", "system"),
                settingsRepository.getString("language", "en"),
                settingsRepository.getBoolean("dynamic_color", true),
                settingsRepository.getInt("buffer_size", 5),
                settingsRepository.getBoolean("hardware_decoding", true),
                settingsRepository.getBoolean("auto_quality", true),
                settingsRepository.getBoolean("background_play", false),
                settingsRepository.getBoolean("picture_in_picture", true),
                settingsRepository.getBoolean("parental_control", false)
            ) { args ->
                SettingsUiState(
                    theme = args[0] as String,
                    language = args[1] as String,
                    dynamicColor = args[2] as Boolean,
                    bufferSize = args[3] as Int,
                    hardwareDecoding = args[4] as Boolean,
                    autoQuality = args[5] as Boolean,
                    backgroundPlay = args[6] as Boolean,
                    pictureInPicture = args[7] as Boolean,
                    parentalControlEnabled = args[8] as Boolean,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setTheme(theme: String) {
        _uiState.update { it.copy(theme = theme) }
        viewModelScope.launch { settingsRepository.setString("theme", theme) }
    }

    fun setLanguage(language: String) {
        _uiState.update { it.copy(language = language) }
        viewModelScope.launch { settingsRepository.setString("language", language) }
    }

    fun setDynamicColor(enabled: Boolean) {
        _uiState.update { it.copy(dynamicColor = enabled) }
        viewModelScope.launch { settingsRepository.setBoolean("dynamic_color", enabled) }
    }

    fun setBufferSize(size: Int) {
        _uiState.update { it.copy(bufferSize = size) }
        viewModelScope.launch { settingsRepository.setInt("buffer_size", size) }
    }

    fun setHardwareDecoding(enabled: Boolean) {
        _uiState.update { it.copy(hardwareDecoding = enabled) }
        viewModelScope.launch { settingsRepository.setBoolean("hardware_decoding", enabled) }
    }

    fun setAutoQuality(enabled: Boolean) {
        _uiState.update { it.copy(autoQuality = enabled) }
        viewModelScope.launch { settingsRepository.setBoolean("auto_quality", enabled) }
    }

    fun setBackgroundPlay(enabled: Boolean) {
        _uiState.update { it.copy(backgroundPlay = enabled) }
        viewModelScope.launch { settingsRepository.setBoolean("background_play", enabled) }
    }

    fun setPictureInPicture(enabled: Boolean) {
        _uiState.update { it.copy(pictureInPicture = enabled) }
        viewModelScope.launch { settingsRepository.setBoolean("picture_in_picture", enabled) }
    }

    fun setParentalControl(enabled: Boolean) {
        _uiState.update { it.copy(parentalControlEnabled = enabled) }
        viewModelScope.launch { settingsRepository.setBoolean("parental_control", enabled) }
    }
}
