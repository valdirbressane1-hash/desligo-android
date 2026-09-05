package com.desligo.analytics

/**
 * Analytics manager — no-op for now.
 * Firebase will be added in the 'full' flavor later.
 */
class AnalyticsManager {

    fun logEvent(event: String, params: Map<String, Any> = emptyMap()) {
        // No-op: will be replaced with Firebase in full flavor
    }

    fun logScreenView(screenName: String) {
        // No-op
    }

    fun setUserId(userId: String?) {
        // No-op
    }

    fun logError(throwable: Throwable) {
        // No-op
    }
}
