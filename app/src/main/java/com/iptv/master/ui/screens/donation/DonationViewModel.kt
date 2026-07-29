package com.iptv.master.ui.screens.donation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iptv.master.domain.model.DonationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DonationUiState(
    val donationInfo: DonationInfo? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class DonationViewModel @Inject constructor(
    donationInfo: DonationInfo
) : ViewModel() {
    private val _uiState = MutableStateFlow(DonationUiState())
    val uiState: StateFlow<DonationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(donationInfo = donationInfo, isLoading = false)
            }
        }
    }

    fun openUrl(url: String) {
        // Will be handled by the UI layer with Intent
    }
}
