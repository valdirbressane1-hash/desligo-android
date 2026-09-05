package com.desligo.profiles

import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val dao: ProfileDao) {

    val allProfiles: Flow<List<Profile>> = dao.getAllProfiles()
    val activeProfile: Flow<Profile?> = dao.getActiveProfile()
    val profileCount: Flow<Int> = dao.getCountFlow()

    suspend fun getProfileById(id: Long): Profile? = dao.getProfileById(id)

    suspend fun getActiveProfileSync(): Profile? = dao.getActiveProfileSync()

    suspend fun insert(profile: Profile): Long = dao.insert(profile)

    suspend fun update(profile: Profile) = dao.update(profile)

    suspend fun delete(profile: Profile) = dao.delete(profile)

    suspend fun activateProfile(profileId: Long) {
        dao.deactivateAll()
        dao.activate(profileId)
    }

    suspend fun deactivateAll() = dao.deactivateAll()

    suspend fun getCount(): Int = dao.getCount()

    suspend fun createDefaultProfiles() {
        val defaults = listOf(
            Profile(name = "Estudo", icon = "📚", color = 0xFF1B5E20, isDefault = true),
            Profile(name = "Sono", icon = "🌙", color = 0xFF1A237E, isDefault = true),
            Profile(name = "Trabalho", icon = "💼", color = 0xFFE65100, isDefault = true),
            Profile(name = "Livre", icon = "🔓", color = 0xFF6750A4, isDefault = true)
        )
        defaults.forEach { insert(it) }
    }
}
