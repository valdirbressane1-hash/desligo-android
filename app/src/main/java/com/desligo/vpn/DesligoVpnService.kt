package com.desligo.vpn

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.desligo.App
import com.desligo.MainActivity
import com.desligo.R
import com.desligo.data.AppDatabase
import com.desligo.data.PreferencesManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class DesligoVpnService : VpnService() {

    companion object {
        private const val TAG = "DesligoVPN"
        var isRunning = false
            private set
    }

    private var vpnInterface: ParcelFileDescriptor? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var blockedPackages = setOf<String>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "STOP" -> {
                stopVpn()
                return START_NOT_STICKY
            }
            "UPDATE_BLOCKED" -> {
                scope.launch { updateBlockedApps() }
                return START_STICKY
            }
        }

        scope.launch {
            loadBlockedApps()
            startVpn()
        }

        return START_STICKY
    }

    private suspend fun loadBlockedApps() {
        try {
            val db = AppDatabase.getInstance(this)
            val profileDao = db.profileDao()
            val activeProfile = profileDao.getActiveProfileSync()

            blockedPackages = activeProfile
                ?.getBlockedAppsList()
                ?.toSet()
                ?: emptySet()

            Log.d(TAG, "Blocked apps: $blockedPackages")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading blocked apps", e)
        }
    }

    private fun startVpn() {
        if (isRunning) return

        try {
            val builder = Builder()
                .setSession("Desligo")
                .setMtu(1500)
                .addAddress("10.0.0.2", 32)
                .addRoute("0.0.0.0", 0)
                .setBlocking(false)

            // Allow apps that are NOT blocked
            val pm = packageManager
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

            for (app in installedApps) {
                if (app.packageName == packageName) continue
                if (app.packageName !in blockedPackages) {
                    try {
                        builder.addAllowedApplication(app.packageName)
                    } catch (e: Exception) {
                        Log.w(TAG, "Could not allow app: ${app.packageName}")
                    }
                }
            }

            // Block specific apps by NOT adding them to allowed
            // VpnService with addAllowedApplication blocks everything not listed

            vpnInterface = builder.establish()

            if (vpnInterface != null) {
                isRunning = true
                startForeground(App.VPN_NOTIFICATION_ID, createNotification())
                Log.d(TAG, "VPN started with ${blockedPackages.size} blocked apps")
            } else {
                Log.e(TAG, "VPN interface is null")
                stopSelf()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting VPN", e)
            stopSelf()
        }
    }

    private fun stopVpn() {
        isRunning = false
        vpnInterface?.close()
        vpnInterface = null
        scope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private suspend fun updateBlockedApps() {
        loadBlockedApps()
        if (isRunning) {
            stopVpn()
            delay(500)
            startVpn()
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, DesligoVpnService::class.java).apply {
            action = "STOP"
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, App.VPN_CHANNEL_ID)
            .setContentTitle("Desligo ativo")
            .setContentText("${blockedPackages.size} apps bloqueados")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .addAction(
                Notification.Action.Builder(
                    null, "Desativar", stopPendingIntent
                ).build()
            )
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    override fun onRevoke() {
        stopVpn()
        super.onRevoke()
    }
}
