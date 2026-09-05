package com.desligo.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.desligo.data.AppDatabase
import com.desligo.data.PreferencesManager
import com.desligo.profiles.Profile
import com.desligo.profiles.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileDetailUiState(
    val profile: Profile? = null,
    val blockedAppsCount: Int = 0,
    val isActive: Boolean = false,
    val isLoading: Boolean = true
)

class ProfileDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = ProfileRepository(db.profileDao())
    private val prefs = PreferencesManager(application)

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    fun loadProfile(profileId: Long) {
        viewModelScope.launch {
            val profile = repo.getProfileById(profileId)
            val activeId = prefs.activeProfileId.first()
            _uiState.value = ProfileDetailUiState(
                profile = profile,
                blockedAppsCount = profile?.getBlockedAppsList()?.size ?: 0,
                isActive = profile?.id == activeId,
                isLoading = false
            )
        }
    }

    fun activateProfile(profileId: Long) {
        viewModelScope.launch {
            repo.activateProfile(profileId)
            prefs.setActiveProfileId(profileId)
            loadProfile(profileId)
        }
    }

    fun deactivateProfile() {
        viewModelScope.launch {
            repo.deactivateAll()
            prefs.setActiveProfileId(-1)
            _uiState.value = _uiState.value.copy(isActive = false)
        }
    }

    fun updateBlockedApps(profileId: Long, packages: List<String>) {
        viewModelScope.launch {
            val profile = repo.getProfileById(profileId) ?: return@launch
            val updated = profile.withBlockedApps(packages)
            repo.update(updated)
            loadProfile(profileId)
        }
    }
}
