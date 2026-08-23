package com.luisbarragan.tallerrecepcion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import com.luisbarragan.tallerrecepcion.ui.components.EstadoBadge
import com.luisbarragan.tallerrecepcion.ui.components.LogoLB
import com.luisbarragan.tallerrecepcion.ui.util.comoFecha
import com.luisbarragan.tallerrecepcion.ui.util.comoKilometraje
import com.luisbarragan.tallerrecepcion.ui.viewmodel.DashboardUiState
import com.luisbarragan.tallerrecepcion.ui.viewmodel.ordenesVisibles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onFiltroCambiado: (String) -> Unit,
    onNuevaRecepcion: () -> Unit,
    onAbrirOrden: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LogoLB()
                        Spacer(Modifier.size(10.dp))
                        Column {
                            Text("Taller Recepción", fontWeight = FontWeight.Bold)
                            Text(
                                "Control de ingreso de vehículos",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNuevaRecepcion,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva recepción") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1486262715619-67b85e0b08d3?auto=format&fit=crop&w=1200&q=80",
                    contentDescription = "Zona de trabajo de un taller automotriz",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Text(
                    text = "Resumen del taller",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(EstadoOrden.entries) { estado ->
                        ResumenEstado(
                            estado = estado,
                            cantidad = uiState.cantidades[estado] ?: 0
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Órdenes recientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.filtro,
                    onValueChange = onFiltroCambiado,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Buscar orden") },
                    placeholder = { Text("Cliente, placa, marca u OT") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
                )
            }

            if (uiState.ordenes.isEmpty()) {
                item { EstadoVacio(onNuevaRecepcion) }
            } else if (uiState.ordenesVisibles.isEmpty()) {
                item {
                    Text(
                        "No hay órdenes que coincidan con la búsqueda.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 18.dp)
                    )
                }
            } else {
                items(uiState.ordenesVisibles, key = { it.id }) { orden ->
                    OrdenCard(orden = orden, onClick = { onAbrirOrden(orden.id) })
                }
            }
        }
    }
}

@Composable
private fun ResumenEstado(
    estado: EstadoOrden,
    cantidad: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = cantidad.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            EstadoBadge(estado)
        }
    }
}

@Composable
private fun OrdenCard(
    orden: OrdenTrabajo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = orden.numeroOrden,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                EstadoBadge(orden.estado)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.size(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = orden.placa,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = listOf(orden.marca, orden.modelo).filter { it.isNotBlank() }.joinToString(" "),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = orden.kilometraje.comoKilometraje(),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.size(7.dp))
                Text(
                    text = orden.cliente,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = orden.fechaIngreso.comoFecha(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EstadoVacio(onNuevaRecepcion: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNuevaRecepcion),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.DirectionsCar,
                    contentDescription = null,
                    modifier = Modifier.size(42.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                Text("Todavía no hay recepciones", fontWeight = FontWeight.SemiBold)
                Text(
                    "Pulsa aquí para registrar la primera.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
