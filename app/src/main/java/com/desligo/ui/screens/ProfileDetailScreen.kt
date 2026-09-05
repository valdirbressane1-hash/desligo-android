package com.desligo.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    profileId: Long,
    onBack: () -> Unit,
    onManageApps: () -> Unit,
    viewModel: ProfileDetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(profileId) {
        viewModel.loadProfile(profileId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.profile?.name ?: "Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val profile = state.profile ?: return@Scaffold

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile header
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(profile.icon, style = MaterialTheme.typography.displayLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(profile.name, style = MaterialTheme.typography.headlineMedium)
                        Text(
                            "${state.blockedAppsCount} apps bloqueados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Actions
                Card(Modifier.fillMaxWidth()) {
                    Column {
                        ListItem(
                            headlineContent = { Text("Gerenciar apps bloqueados") },
                            supportingContent = { Text("Selecionar quais apps bloquear neste perfil") },
                            leadingContent = { Icon(Icons.Default.Apps, null) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, null) },
                            modifier = Modifier.clickable(onClick = onManageApps)
                        )

                        Divider()

                        ListItem(
                            headlineContent = { Text("Bloquear chamadas") },
                            supportingContent = { Text("Bloquear chamadas enquanto este perfil está ativo") },
                            leadingContent = { Icon(Icons.Default.Phone, null) },
                            trailingContent = {
                                Switch(
                                    checked = profile.blockCalls,
                                    onCheckedChange = { /* TODO */ }
                                )
                            }
                        )
                    }
                }

                // Activation button
                Button(
                    onClick = {
                        if (state.isActive) {
                            viewModel.deactivateProfile()
                        } else {
                            viewModel.activateProfile(profileId)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (state.isActive) ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ) else ButtonDefaults.buttonColors()
                ) {
                    Icon(
                        if (state.isActive) Icons.Default.Stop else Icons.Default.PlayArrow,
                        null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (state.isActive) "Desativar Perfil" else "Ativar Perfil")
                }
            }
        }
    }
}
