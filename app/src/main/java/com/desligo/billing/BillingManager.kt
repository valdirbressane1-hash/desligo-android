package com.desligo.billing

import android.app.Activity
import android.content.Context
import android.util.Log

/**
 * Play Billing manager - only used in 'full' flavor.
 * Placeholder implementation for future Play Store integration.
 */
class BillingManager(private val context: Context) {

    companion object {
        private const val TAG = "BillingManager"
        const val SKU_PREMIUM_MONTHLY = "desligo_premium_monthly"
        const val SKU_PREMIUM_LIFETIME = "desligo_premium_lifetime"
    }

    interface BillingCallback {
        fun onPurchaseSuccess(sku: String)
        fun onPurchaseFailed(error: String)
    }

    private var callback: BillingCallback? = null

    fun initialize(callback: BillingCallback) {
        this.callback = callback
        // TODO: Initialize BillingClient when Play Store is available
        Log.d(TAG, "BillingManager initialized (placeholder)")
    }

    fun launchPurchaseFlow(activity: Activity, sku: String) {
        // TODO: Launch billing flow when Play Store is available
        Log.d(TAG, "Launch purchase flow for $sku (placeholder)")
    }

    fun queryPurchases() {
        // TODO: Query existing purchases
        Log.d(TAG, "Query purchases (placeholder)")
    }

    fun destroy() {
        // TODO: End billing client connection
    }
}
