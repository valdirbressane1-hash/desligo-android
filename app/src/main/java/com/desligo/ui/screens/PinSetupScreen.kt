package com.desligo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.desligo.data.PreferencesManager
import com.desligo.util.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinSetupScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { PreferencesManager(context) }

    var step by remember { mutableStateOf("enter") } // enter, confirm, question, answer
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var securityQuestion by remember { mutableStateOf("") }
    var securityAnswer by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var success by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar PIN") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (step) {
                "enter" -> {
                    Icon(Icons.Default.Lock, null, Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Crie um PIN de ${Constants.PIN_LENGTH} dígitos",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { if (it.length <= Constants.PIN_LENGTH) pin = it.filter { c -> c.isDigit() } },
                        label = { Text("PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (pin.length == Constants.PIN_LENGTH) {
                                step = "confirm"
                                error = null
                            } else {
                                error = "PIN deve ter ${Constants.PIN_LENGTH} dígitos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = pin.length == Constants.PIN_LENGTH
                    ) {
                        Text("Próximo")
                    }
                }

                "confirm" -> {
                    Icon(Icons.Default.Lock, null, Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Confirme seu PIN",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { if (it.length <= Constants.PIN_LENGTH) confirmPin = it.filter { c -> c.isDigit() } },
                        label = { Text("Confirmar PIN") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { step = "enter"; confirmPin = "" },
                            modifier = Modifier.weight(1f)
                        ) { Text("Voltar") }
                        Button(
                            onClick = {
                                if (confirmPin == pin) {
                                    step = "question"
                                    error = null
                                } else {
                                    error = "PINs não coincidem"
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = confirmPin.length == Constants.PIN_LENGTH
                        ) { Text("Próximo") }
                    }
                }

                "question" -> {
                    Icon(Icons.Default.Help, null, Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Pergunta de segurança",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "Para recuperar o PIN caso esqueça",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextField(
                        value = securityQuestion,
                        onValueChange = { securityQuestion = it },
                        label = { Text("Pergunta (ex: nome do primeiro pet)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = securityAnswer,
                        onValueChange = { securityAnswer = it },
                        label = { Text("Resposta") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (securityQuestion.isNotBlank() && securityAnswer.isNotBlank()) {
                                scope.launch {
                                    prefs.setPin(pin)
                                    prefs.setSecurityQuestion(securityQuestion, securityAnswer)
                                    success = true
                                }
                            } else {
                                error = "Preencha todos os campos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = securityQuestion.isNotBlank() && securityAnswer.isNotBlank()
                    ) {
                        Text("Salvar PIN")
                    }
                }
            }

            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            if (success) {
                Spacer(Modifier.height(16.dp))
                Icon(
                    Icons.Default.CheckCircle,
                    null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("PIN configurado com sucesso!")
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("Voltar")
                }
            }
        }
    }
}
