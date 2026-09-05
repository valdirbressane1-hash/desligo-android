package com.desligo.vpn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Check if VPN was active before reboot
            // For now, we don't auto-start on boot
            // Users can enable this as a preference later
        }
    }
}
