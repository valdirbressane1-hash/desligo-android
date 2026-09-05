package com.desligo.calls

import android.telecom.Call
import android.telecom.CallScreeningService

class CallBlockerService : CallScreeningService() {

    companion object {
        var blockedNumbers: Set<String> = emptySet()
        var blockAllCalls: Boolean = false
    }

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: return

        val shouldBlock = blockAllCalls || phoneNumber in blockedNumbers

        val response = CallResponse.Builder()
            .setDisallowCall(shouldBlock)
            .setRejectCall(shouldBlock)
            .setSkipCallLog(shouldBlock)
            .setSkipNotification(shouldBlock)
            .build()

        respondToCall(callDetails, response)
    }
}
