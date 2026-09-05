package com.desligo.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.desligo.profiles.Profile
import com.desligo.vpn.VpnManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProfileClick: (Long) -> Unit,
    onTimerClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onPremiumClick: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Desligo") },
                actions = {
                    if (!state.isPremium && !state.isTrialActive) {
                        IconButton(onClick = onPremiumClick) {
                            Icon(Icons.Default.Star, "Premium")
                        }
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, "Configurações")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Trial/Premium banner
            if (!state.isPremium) {
                item {
                    TrialBanner(
                        isTrialActive = state.isTrialActive,
                        daysRemaining = state.trialDaysRemaining,
                        onClick = onPremiumClick
                    )
                }
            }

            // VPN Status
            item {
                VpnStatusCard(
                    isActive = state.isVpnActive,
                    activeProfileName = state.profiles.find { it.id == state.activeProfileId }?.name,
                    onToggle = {
                        if (state.isVpnActive) {
                            viewModel.deactivateAll()
                        }
                    }
                )
            }

            // Section: Perfis
            item {
                Text(
                    "Perfis",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(state.profiles) { profile ->
                ProfileCard(
                    profile = profile,
                    isActive = profile.id == state.activeProfileId,
                    onClick = {
                        if (profile.id == state.activeProfileId) {
                            viewModel.deactivateAll()
                        } else {
                            viewModel.activateProfile(profile.id)
                        }
                    },
                    onDetailClick = { onProfileClick(profile.id) }
                )
            }

            // Timer shortcut
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedCard(
                    onClick = onTimerClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Timer,
                            "Timer",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pomodoro Timer", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Sessões focadas de 25min",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(Icons.Default.ChevronRight, null)
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun TrialBanner(isTrialActive: Boolean, daysRemaining: Long, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isTrialActive)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isTrialActive) Icons.Default.Timer else Icons.Default.Lock,
                null,
                tint = if (isTrialActive)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isTrialActive) "Trial: $daysRemaining dias restantes" else "Trial expirado",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (isTrialActive) "Aproveite todos os recursos!" else "Ative o Premium para continuar",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(Icons.Default.ChevronRight, null)
        }
    }
}

@Composable
fun VpnStatusCard(isActive: Boolean, activeProfileName: String?, onToggle: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                null,
                tint = if (isActive)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (isActive) "VPN Ativo" else "VPN Inativo",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                if (isActive && activeProfileName != null) {
                    Text(
                        "Perfil: $activeProfileName",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            if (isActive) {
                IconButton(onClick = onToggle) {
                    Icon(Icons.Default.Stop, "Desativar")
                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    profile: Profile,
    isActive: Boolean,
    onClick: () -> Unit,
    onDetailClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(profile.icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    profile.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                val blockedCount = profile.getBlockedAppsList().size
                Text(
                    if (blockedCount > 0) "$blockedCount apps bloqueados" else "Nenhum app bloqueado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isActive) {
                Icon(
                    Icons.Default.CheckCircle,
                    "Ativo",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDetailClick) {
                Icon(Icons.Default.Edit, "Editar")
            }
        }
    }
}
