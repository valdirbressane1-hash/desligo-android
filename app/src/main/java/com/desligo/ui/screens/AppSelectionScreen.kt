package com.desligo.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.desligo.apps.AppInfo
import com.desligo.apps.AppListManager
import com.desligo.data.AppDatabase
import com.desligo.profiles.ProfileRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectionScreen(
    profileId: Long,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val appListManager = remember { AppListManager(context) }
    val db = remember { AppDatabase.getInstance(context) }
    val repo = remember { ProfileRepository(db.profileDao()) }

    var allApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var blockedPackages by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(profileId) {
        val profile = repo.getProfileById(profileId)
        blockedPackages = profile?.getBlockedAppsList()?.toSet() ?: emptySet()
        allApps = appListManager.getInstalledApps().map { app ->
            app.copy(isBlocked = app.packageName in blockedPackages)
        }
        isLoading = false
    }

    val filteredApps = remember(allApps, searchQuery) {
        if (searchQuery.isBlank()) allApps
        else allApps.filter {
            it.appName.contains(searchQuery, ignoreCase = true) ||
            it.packageName.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecionar Apps") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    Text(
                        "${blockedPackages.size} bloqueados",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar apps...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, "Limpar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(filteredApps) { app ->
                        ListItem(
                            headlineContent = { Text(app.appName) },
                            supportingContent = { Text(app.packageName) },
                            leadingContent = {
                                Icon(Icons.Default.Android, null)
                            },
                            trailingContent = {
                                Checkbox(
                                    checked = app.packageName in blockedPackages,
                                    onCheckedChange = { isChecked ->
                                        blockedPackages = if (isChecked) {
                                            blockedPackages + app.packageName
                                        } else {
                                            blockedPackages - app.packageName
                                        }
                                        // Save to database
                                        scope.launch {
                                            repo.getProfileById(profileId)?.let { profile ->
                                                repo.update(
                                                    profile.withBlockedApps(blockedPackages.toList())
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
