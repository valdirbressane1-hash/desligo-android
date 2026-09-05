package com.desligo.profiles

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String = "📱",
    val color: Long = 0xFF6750A4,
    val isDefault: Boolean = false,
    val isActive: Boolean = false,
    val blockedAppPackages: String = "", // comma-separated
    val blockCalls: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getBlockedAppsList(): List<String> =
        if (blockedAppPackages.isBlank()) emptyList()
        else blockedAppPackages.split(",").filter { it.isNotBlank() }

    fun withBlockedApps(packages: List<String>): Profile =
        copy(blockedAppPackages = packages.joinToString(","))
}
