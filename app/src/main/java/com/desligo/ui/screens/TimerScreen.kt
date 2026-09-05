package com.desligo.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desligo.timer.PomodoroTimer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(onBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val timer = remember { PomodoroTimer(context) }
    val state by timer.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pomodoro Timer") },
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Session counter
            Text(
                "Sessão ${state.completedSessions + 1}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            // Status label
            Text(
                if (state.isBreak) "Pausa" else if (state.isRunning) "Focando..." else "Pronto",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(32.dp))

            // Circular progress
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(240.dp)) {
                CircularProgressIndicator(
                    progress = state.progress,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 8.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    state.displayTime,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(48.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isRunning || state.isPaused) {
                    // Stop button
                    FilledTonalIconButton(
                        onClick = { timer.stop() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Stop, "Parar", Modifier.size(28.dp))
                    }

                    // Pause/Resume
                    FilledIconButton(
                        onClick = {
                            if (state.isRunning) timer.pause() else timer.start()
                        },
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(
                            if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            if (state.isRunning) "Pausar" else "Continuar",
                            Modifier.size(36.dp)
                        )
                    }

                    // Skip
                    FilledTonalIconButton(
                        onClick = { timer.skip() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.SkipNext, "Pular", Modifier.size(28.dp))
                    }
                } else {
                    // Start button
                    Button(
                        onClick = { timer.start() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (state.isBreak) "Iniciar Pausa" else "Iniciar Foco",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Completed sessions
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { index ->
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (index < state.completedSessions)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(12.dp)
                    ) {}
                }
            }
        }
    }
}
