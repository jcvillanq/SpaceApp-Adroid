package com.lasalle.spaceapps.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lasalle.spaceapps.data.model.Rocket
import com.lasalle.spaceapps.ui.viewmodels.RocketListUiState
import com.lasalle.spaceapps.ui.viewmodels.RocketListViewModel
import com.lasalle.spaceapps.ui.viewmodels.RocketListViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketListScreen(
    onNavigateToDetail: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: RocketListViewModel = viewModel(
        factory = RocketListViewModelFactory (
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Cohetes") },
                actions = {
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.semantics {
                            contentDescription = "Cerrar sesión"
                        }
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Campo de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .semantics {
                        contentDescription = "Campo de búsqueda de cohetes por nombre"
                    },
                placeholder = { Text("Buscar cohete....") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Icono de búsqueda")
                },
                singleLine = true
            )

            // Contenido según el estado
            when (val state = uiState) {
                is RocketListUiState.Loading -> {
                    LoadingState()
                }
                is RocketListUiState.Success -> {
                    RocketList(
                        rockets = state.rockets,
                        onRocketClick = onNavigateToDetail
                    )
                }
                is RocketListUiState.Empty -> {
                    EmptyState()
                }
                is RocketListUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadRockets() }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "Cargando lista de cohetes, por favor espera"
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando cohetes disponibles...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "No se encontraron cohetes que coincidan con tu búsqueda"
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Icono de búsqueda vacía",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sin resultados",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No se encontraron cohetes que coincidan con tu búsqueda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "Error al cargar cohetes. $message. Pulsa el botón reintentar para volver a cargar"
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Icono de error",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error al cargar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No pudimos cargar los cohetes. Por favor, verifica tu conexión a internet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.semantics {
                    contentDescription = "Botón reintentar carga de cohetes"
                }
            ) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun RocketList(
    rockets: List<Rocket>,
    onRocketClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "Lista de ${rockets.size} cohetes disponibles"
            },
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rockets) { rocket ->
            RocketCard(
                rocket = rocket,
                onClick = { onRocketClick(rocket.id) }
            )
        }
    }
}

@Composable
fun RocketCard(
    rocket: Rocket,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "Cohete ${rocket.name}, ${if (rocket.active) "activo" else "inactivo"}. Pulsa para ver detalles"
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del cohete
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(rocket.flickrImages.firstOrNull() ?: "")
                    .crossfade(true)
                    .build(),
                contentDescription = "Fotografía del cohete ${rocket.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del cohete
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = rocket.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${rocket.country ?: "Desconocido"} • ${rocket.company ?: "Desconocida"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Badge de estado
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (rocket.active) Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                modifier = Modifier.semantics {
                    contentDescription = if (rocket.active) "Estado: Activo" else "Estado: Inactivo"
                }
            ) {
                Text(
                    text = if (rocket.active) "Activo" else "Inactivo",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}