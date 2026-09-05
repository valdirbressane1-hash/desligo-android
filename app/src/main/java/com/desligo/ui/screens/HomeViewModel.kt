package com.desligo.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.desligo.data.AppDatabase
import com.desligo.data.PreferencesManager
import com.desligo.profiles.Profile
import com.desligo.profiles.ProfileRepository
import com.desligo.vpn.VpnManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val profiles: List<Profile> = emptyList(),
    val activeProfileId: Long = -1,
    val isVpnActive: Boolean = false,
    val isPremium: Boolean = false,
    val isTrialActive: Boolean = true,
    val trialDaysRemaining: Long = 15,
    val isLoading: Boolean = true
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = ProfileRepository(db.profileDao())
    private val prefs = PreferencesManager(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Init install date
            prefs.initInstallDate()

            // Create defaults if first run
            if (repo.getCount() == 0) {
                repo.createDefaultProfiles()
            }

            // Observe profiles
            combine(
                repo.allProfiles,
                prefs.activeProfileId,
                prefs.isPremium,
                prefs.isTrialActive,
                prefs.trialDaysRemaining
            ) { profiles, activeId, premium, trial, days ->
                HomeUiState(
                    profiles = profiles,
                    activeProfileId = activeId,
                    isVpnActive = VpnManager.isVpnActive(),
                    isPremium = premium,
                    isTrialActive = trial,
                    trialDaysRemaining = days,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun activateProfile(profileId: Long) {
        viewModelScope.launch {
            repo.activateProfile(profileId)
            prefs.setActiveProfileId(profileId)
            VpnManager.updateBlockedApps(getApplication())
            if (!VpnManager.isVpnActive()) {
                VpnManager.startVpn(getApplication())
            }
        }
    }

    fun deactivateAll() {
        viewModelScope.launch {
            repo.deactivateAll()
            prefs.setActiveProfileId(-1)
            VpnManager.stopVpn(getApplication())
        }
    }
}
