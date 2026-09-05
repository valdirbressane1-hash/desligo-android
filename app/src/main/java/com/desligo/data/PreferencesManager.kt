package com.desligo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.desligo.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import java.security.SecureRandom

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.DATASTORE_NAME
)

class PreferencesManager(private val context: Context) {

    companion object {
        private val PIN_HASH = stringPreferencesKey("pin_hash")
        private val PIN_SALT = stringPreferencesKey("pin_salt")
        private val SECURITY_QUESTION = stringPreferencesKey("security_question")
        private val SECURITY_ANSWER_HASH = stringPreferencesKey("security_answer_hash")
        private val INSTALL_DATE = longPreferencesKey("install_date")
        private val PREMIUM_ACTIVATED = booleanPreferencesKey("premium_activated")
        private val PREMIUM_CODE = stringPreferencesKey("premium_code")
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val DARK_THEME = booleanPreferencesKey("dark_theme")
        private val ACTIVE_PROFILE_ID = longPreferencesKey("active_profile_id")
    }

    // --- Onboarding ---

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { it[ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { it[ONBOARDING_COMPLETED] = true }
    }

    // --- PIN ---

    val hasPinSet: Flow<Boolean> = context.dataStore.data
        .map { !it[PIN_HASH].isNullOrBlank() }

    suspend fun setPin(pin: String) {
        val salt = generateSalt()
        val hash = hashPin(pin, salt)
        context.dataStore.edit {
            it[PIN_HASH] = hash
            it[PIN_SALT] = salt
        }
    }

    suspend fun verifyPin(pin: String): Boolean {
        val prefs = context.dataStore.data
        var result = false
        prefs.collect {
            val storedHash = it[PIN_HASH] ?: return@collect
            val salt = it[PIN_SALT] ?: return@collect
            result = hashPin(pin, salt) == storedHash
        }
        return result
    }

    suspend fun clearPin() {
        context.dataStore.edit {
            it.remove(PIN_HASH)
            it.remove(PIN_SALT)
        }
    }

    // --- Security Question ---

    val hasSecurityQuestion: Flow<Boolean> = context.dataStore.data
        .map { !it[SECURITY_QUESTION].isNullOrBlank() }

    suspend fun setSecurityQuestion(question: String, answer: String) {
        val salt = generateSalt()
        val hash = hashPin(answer.lowercase().trim(), salt)
        context.dataStore.edit {
            it[SECURITY_QUESTION] = question
            it[SECURITY_ANSWER_HASH] = hash
            it[PIN_SALT] = salt // reuse salt storage
        }
    }

    suspend fun verifySecurityAnswer(answer: String): Boolean {
        val prefs = context.dataStore.data
        var result = false
        prefs.collect {
            val storedHash = it[SECURITY_ANSWER_HASH] ?: return@collect
            val salt = it[PIN_SALT] ?: return@collect
            result = hashPin(answer.lowercase().trim(), salt) == storedHash
        }
        return result
    }

    // --- Trial & Premium ---

    val installDate: Flow<Long> = context.dataStore.data
        .map { it[INSTALL_DATE] ?: System.currentTimeMillis() }

    val isPremium: Flow<Boolean> = context.dataStore.data
        .map { it[PREMIUM_ACTIVATED] ?: false }

    val trialDaysRemaining: Flow<Long> = context.dataStore.data
        .map { prefs ->
            val install = prefs[INSTALL_DATE] ?: System.currentTimeMillis()
            val elapsed = (System.currentTimeMillis() - install) / (1000 * 60 * 60 * 24)
            (Constants.TRIAL_DAYS - elapsed).coerceAtLeast(0)
        }

    val isTrialActive: Flow<Boolean> = context.dataStore.data
        .map { prefs ->
            val install = prefs[INSTALL_DATE] ?: System.currentTimeMillis()
            val elapsed = (System.currentTimeMillis() - install) / (1000 * 60 * 60 * 24)
            elapsed < Constants.TRIAL_DAYS
        }

    suspend fun initInstallDate() {
        context.dataStore.edit {
            if (it[INSTALL_DATE] == null) {
                it[INSTALL_DATE] = System.currentTimeMillis()
            }
        }
    }

    suspend fun activatePremium(code: String) {
        context.dataStore.edit {
            it[PREMIUM_ACTIVATED] = true
            it[PREMIUM_CODE] = code
        }
    }

    // --- Theme ---

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_THEME] ?: false }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[DARK_THEME] = enabled }
    }

    // --- Active Profile ---

    val activeProfileId: Flow<Long> = context.dataStore.data
        .map { it[ACTIVE_PROFILE_ID] ?: -1 }

    suspend fun setActiveProfileId(id: Long) {
        context.dataStore.edit { it[ACTIVE_PROFILE_ID] = id }
    }

    // --- Utility ---

    private fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun hashPin(pin: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val input = "$salt:$pin".toByteArray()
        val hash = md.digest(input)
        return hash.joinToString("") { "%02x".format(it) }
    }
}
