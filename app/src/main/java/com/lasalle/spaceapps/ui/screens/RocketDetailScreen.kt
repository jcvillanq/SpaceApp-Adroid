package com.lasalle.spaceapps.ui.screens

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.NumberFormat
import java.util.Locale
import com.lasalle.spaceapps.ui.viewmodels.RocketDetailViewModel
import com.lasalle.spaceapps.ui.viewmodels.RocketDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketDetailScreen(
    rocketId: String,
    onNavigateBack: () -> Unit,
    viewModel: RocketDetailViewModel = viewModel(
        factory = RocketDetailViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val rocket by viewModel.rocket.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(rocketId) {
        viewModel.loadRocket(rocketId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Cohete") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        rocket?.let { rocketData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen principal
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(rocketData.flickrImages.firstOrNull() ?: "")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen de ${rocketData.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Nombre y estado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rocketData.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (rocketData.active) Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (rocketData.active) "Activo" else "Inactivo",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text(
                        text = "Descripción",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rocketData.description ?: "Sin descripción disponible",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Especificaciones
                    Text(
                        text = "Especificaciones",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Grid de especificaciones
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SpecificationRow("Primer vuelo", rocketData.firstFlight ?: "N/A")
                            SpecificationRow("País", rocketData.country ?: "N/A")
                            SpecificationRow("Compañía", rocketData.company ?: "N/A")
                            SpecificationRow(
                                "Tasa de éxito",
                                if (rocketData.successRatePct != null) "${rocketData.successRatePct}%" else "N/A"
                            )
                            SpecificationRow(
                                "Costo por lanzamiento",
                                rocketData.costPerLaunch?.let {
                                    NumberFormat.getCurrencyInstance(Locale.US).format(it)
                                } ?: "N/A"
                            )
                            SpecificationRow("Etapas", rocketData.stages?.toString() ?: "N/A")
                            SpecificationRow("Boosters", rocketData.boosters?.toString() ?: "N/A")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dimensiones
                    Text(
                        text = "Dimensiones",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SpecificationRow(
                                "Altura",
                                rocketData.height?.meters?.let { "${it}m" } ?: "N/A"
                            )
                            SpecificationRow(
                                "Diámetro",
                                rocketData.diameter?.meters?.let { "${it}m" } ?: "N/A"
                            )
                            SpecificationRow(
                                "Masa",
                                rocketData.mass?.kg?.let { "${NumberFormat.getInstance().format(it)} kg" } ?: "N/A"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Wikipedia
                    rocketData.wikipedia?.let { wikipediaUrl ->
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Más info (Wikipedia)")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        } ?: run {
            // Estado de carga
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SpecificationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium
        )
    }
}