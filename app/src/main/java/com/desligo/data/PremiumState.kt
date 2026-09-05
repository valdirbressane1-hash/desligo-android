package com.desligo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class PremiumState(
    val isPremium: Boolean = false,
    val isTrialActive: Boolean = false,
    val trialDaysRemaining: Long = 15
) {
    val hasAccess: Boolean get() = isPremium || isTrialActive
    val isExpired: Boolean get() = !isPremium && !isTrialActive
}

fun Flow<Boolean>.combineWith(
    trialActive: Flow<Boolean>,
    trialDays: Flow<Long>
): Flow<PremiumState> {
    return combine(this, trialActive, trialDays) { premium, trial, days ->
        PremiumState(
            isPremium = premium,
            isTrialActive = trial,
            trialDaysRemaining = days
        )
    }
}
