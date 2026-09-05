package com.desligo.vpn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.VpnService

object VpnManager {

    fun startVpn(context: Context) {
        val intent = Intent(context, DesligoVpnService::class.java)
        context.startForegroundService(intent)
    }

    fun stopVpn(context: Context) {
        val intent = Intent(context, DesligoVpnService::class.java).apply {
            action = "STOP"
        }
        context.startService(intent)
    }

    fun updateBlockedApps(context: Context) {
        val intent = Intent(context, DesligoVpnService::class.java).apply {
            action = "UPDATE_BLOCKED"
        }
        context.startService(intent)
    }

    fun prepareVpn(activity: Activity): Intent? {
        return VpnService.prepare(activity)
    }

    fun isVpnActive(): Boolean = DesligoVpnService.isRunning
}
