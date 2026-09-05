package com.desligo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.desligo.billing.PixActivationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val pixManager = remember { PixActivationManager(context) }
    val paymentInfo = remember { pixManager.getPixPaymentInfo() }
    var activationCode by remember { mutableStateOf("") }
    var activationResult by remember { mutableStateOf<String?>(null) }
    var showPixInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Premium") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Icon(
                Icons.Default.Star,
                null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Desligo Premium",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Desbloqueie todo o poder do Desligo",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // Features list
            val features = listOf(
                "🎮 Modo Gaming — 1 toque para jogar",
                "⏰ Agendamento — perfis automáticos por horário",
                "📞 Bloqueio de chamadas",
                "🌙 Tema escuro",
                "📤 Exportar/Importar configurações",
                "🎯 Modo Foco completo",
                "♾️ Perfis ilimitados"
            )

            features.forEach { feature ->
                ListItem(
                    headlineContent = { Text(feature) },
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Activate button
            Button(
                onClick = { showPixInfo = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Payment, null)
                Spacer(Modifier.width(8.dp))
                Text("Ativar com Pix")
            }

            Spacer(Modifier.height(16.dp))

            // Activation code field
            OutlinedTextField(
                value = activationCode,
                onValueChange = { activationCode = it.uppercase() },
                label = { Text("Código de ativação") },
                placeholder = { Text("XXXXXXXXXXXX") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    // TODO: validate code via PixActivationManager
                    activationResult = "Código verificado!"
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = activationCode.length >= 8
            ) {
                Text("Ativar")
            }

            activationResult?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.primary)
            }

            // Pix info dialog
            if (showPixInfo) {
                AlertDialog(
                    onDismissRequest = { showPixInfo = false },
                    title = { Text("Pagamento via Pix") },
                    text = {
                        Column {
                            Text("Chave Pix (CPF):")
                            Text(
                                paymentInfo.pixKey,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text("Titular: ${paymentInfo.holderName}")
                            Spacer(Modifier.height(16.dp))
                            Text("Passo a passo:")
                            paymentInfo.instructions.forEachIndexed { index, instruction ->
                                Text("${index + 1}. $instruction")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showPixInfo = false }) {
                            Text("Fechar")
                        }
                    }
                )
            }
        }
    }
}
