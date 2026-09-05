package com.desligo.billing

import android.content.Context
import com.desligo.data.PreferencesManager
import java.util.UUID

class PixActivationManager(private val context: Context) {

    private val prefs = PreferencesManager(context)

    /**
     * Generate a unique activation code.
     * Called by Valdir after receiving Pix payment.
     */
    fun generateActivationCode(): String {
        return UUID.randomUUID().toString().uppercase().take(12)
    }

    /**
     * Validate an activation code entered by the user.
     * For MVP: accept any 12-char code (offline validation).
     * Future: validate against a server or local database of valid codes.
     */
    suspend fun activateWithCode(code: String): Boolean {
        if (code.length < 8) return false

        // MVP: accept any valid-looking code
        // In production, this would check against a server
        val cleanCode = code.trim().uppercase()
        if (cleanCode.length !in 8..12) return false

        prefs.activatePremium(cleanCode)
        return true
    }

    /**
     * Get the Pix payment info to show in the app.
     */
    fun getPixPaymentInfo(): PixPaymentInfo {
        return PixPaymentInfo(
            pixKey = "195.752.858-32",
            holderName = "Valdir Aparecido dos Santos",
            instructions = listOf(
                "Copie a chave Pix acima",
                "Abra seu app bancário e faça o pagamento",
                "Envie o comprovante para: desligo.app@gmail.com",
                "Aguarde o código de ativação (até 24h)",
                "Insira o código no campo abaixo"
            )
        )
    }
}

data class PixPaymentInfo(
    val pixKey: String,
    val holderName: String,
    val instructions: List<String>
)
