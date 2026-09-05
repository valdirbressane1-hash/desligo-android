package com.desligo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.desligo.manufacturer.ManufacturerDetector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onPremiumClick: () -> Unit,
    onManufacturerGuide: () -> Unit,
    onPinSetup: () -> Unit
) {
    val guide = remember { ManufacturerDetector.getGuide() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
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
        ) {
            // Premium
            Card(
                onClick = onPremiumClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Star, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Desligo Premium", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Desbloqueie todos os recursos",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Security section
            Text(
                "Segurança",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ListItem(
                headlineContent = { Text("Configurar PIN") },
                supportingContent = { Text("Proteger configurações com PIN") },
                leadingContent = { Icon(Icons.Default.Lock, null) },
                modifier = Modifier.clickable(onClick = onPinSetup)
            )

            Divider()

            // Device section
            Text(
                "Dispositivo",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ListItem(
                headlineContent = { Text("Guia: ${guide.manufacturer}") },
                supportingContent = { Text("Otimizar bateria para este dispositivo") },
                leadingContent = { Icon(Icons.Default.PhoneAndroid, null) },
                modifier = Modifier.clickable(onClick = onManufacturerGuide)
            )

            Divider()

            // About section
            Text(
                "Sobre",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ListItem(
                headlineContent = { Text("Versão") },
                supportingContent = { Text("1.0.0") },
                leadingContent = { Icon(Icons.Default.Info, null) }
            )

            ListItem(
                headlineContent = { Text("Política de Privacidade") },
                leadingContent = { Icon(Icons.Default.PrivacyTip, null) }
            )

            ListItem(
                headlineContent = { Text("Termos de Uso") },
                leadingContent = { Icon(Icons.Default.Description, null) }
            )
        }
    }
}
