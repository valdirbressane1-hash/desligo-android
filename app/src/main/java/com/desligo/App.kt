package com.desligo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {

    companion object {
        const val VPN_CHANNEL_ID = "desligo_vpn"
        const val TIMER_CHANNEL_ID = "desligo_timer"
        const val VPN_NOTIFICATION_ID = 1
        const val TIMER_NOTIFICATION_ID = 2
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val vpnChannel = NotificationChannel(
            VPN_CHANNEL_ID,
            "Desligo VPN",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notificação do serviço VPN"
            setShowBadge(false)
        }

        val timerChannel = NotificationChannel(
            TIMER_CHANNEL_ID,
            "Desligo Timer",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notificação do timer Pomodoro"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(vpnChannel)
        manager.createNotificationChannel(timerChannel)
    }
}
