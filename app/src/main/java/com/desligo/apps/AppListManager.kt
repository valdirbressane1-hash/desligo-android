package com.desligo.apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class AppListManager(private val context: Context) {

    fun getInstalledApps(): List<AppInfo> {
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        return packages
            .filter { it.packageName != context.packageName }
            .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 || isUserSystemApp(it) }
            .map { appInfo ->
                AppInfo(
                    packageName = appInfo.packageName,
                    appName = pm.getApplicationLabel(appInfo).toString(),
                    icon = try { pm.getApplicationIcon(appInfo) } catch (_: Exception) { null },
                    isSystemApp = appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    private fun isUserSystemApp(appInfo: ApplicationInfo): Boolean {
        // Allow some "system" apps that users actually interact with
        val allowedPrefixes = listOf(
            "com.android.chrome",
            "com.google.android.youtube",
            "com.google.android.gm",
            "com.android.gallery",
            "com.android.camera"
        )
        return allowedPrefixes.any { appInfo.packageName.startsWith(it) }
    }

    fun getAppName(packageName: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (_: Exception) {
            packageName
        }
    }
}
