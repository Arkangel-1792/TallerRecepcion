package com.luisbarragan.tallerrecepcion.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import com.luisbarragan.tallerrecepcion.ui.components.EstadoBadge
import com.luisbarragan.tallerrecepcion.ui.util.comoFecha
import com.luisbarragan.tallerrecepcion.ui.util.comoKilometraje
import com.luisbarragan.tallerrecepcion.ui.viewmodel.DetalleOrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleOrdenScreen(
    viewModel: DetalleOrdenViewModel,
    onVolver: () -> Unit
) {
    val orden by viewModel.orden.collectAsStateWithLifecycle()
    val eliminada by viewModel.eliminada.collectAsStateWithLifecycle()
    var confirmarEliminacion by remember { mutableStateOf(false) }

    LaunchedEffect(eliminada) {
        if (eliminada) onVolver()
    }

    if (confirmarEliminacion) {
        AlertDialog(
            onDismissRequest = { confirmarEliminacion = false },
            title = { Text("Eliminar orden") },
            text = { Text("Esta recepción se eliminará del historial del dispositivo.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarEliminacion = false
                        viewModel.eliminarOrden()
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarEliminacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(orden?.numeroOrden ?: "Detalle de orden") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (orden != null) {
                        IconButton(onClick = { confirmarEliminacion = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar orden",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val ordenActual = orden
        if (ordenActual == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            ContenidoDetalle(
                orden = ordenActual,
                onAvanzarEstado = viewModel::avanzarEstado,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ContenidoDetalle(
    orden: OrdenTrabajo,
    onAvanzarEstado: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                orden.placa,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                listOf(orden.marca, orden.modelo)
                                    .filter { it.isNotBlank() }
                                    .joinToString(" "),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        EstadoBadge(orden.estado)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Ingreso: ${orden.fechaIngreso.comoFecha()}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        item {
            Text("Flujo de la orden", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadoOrden.entries.forEach { estado ->
                    EstadoBadge(estado = estado)
                }
            }
        }

        item {
            TarjetaInformacion("Cliente") {
                FilaDato(Icons.Default.Person, "Nombre", orden.cliente)
                if (orden.empresa.isNotBlank()) {
                    FilaDato(null, "Empresa", orden.empresa)
                }
                if (orden.telefono.isNotBlank()) {
                    FilaDato(Icons.Default.Phone, "Teléfono", orden.telefono)
                }
                if (orden.email.isNotBlank()) {
                    FilaDato(null, "Correo", orden.email)
                }
                if (orden.direccion.isNotBlank()) {
                    FilaDato(null, "Dirección", orden.direccion)
                }
            }
        }

        item {
            TarjetaInformacion("Vehículo y recepción") {
                if (orden.tipoVehiculo.isNotBlank()) {
                    FilaDato(Icons.Default.DirectionsCar, "Tipo", orden.tipoVehiculo)
                }
                if (orden.color.isNotBlank()) {
                    FilaDato(null, "Color", orden.color)
                }
                if (orden.numeroSerie.isNotBlank()) {
                    FilaDato(null, "Serie / VIN", orden.numeroSerie)
                }
                if (orden.numeroMotor.isNotBlank()) {
                    FilaDato(null, "N.º de motor", orden.numeroMotor)
                }
                FilaDato(Icons.Default.DirectionsCar, "Servicio", orden.tipoServicio)
                FilaDato(Icons.Default.Speed, "Kilometraje", orden.kilometraje.comoKilometraje())
                FilaDato(null, "Combustible", orden.nivelCombustible)
                if (orden.fechaEntregaEstimada.isNotBlank()) {
                    FilaDato(null, "Entrega estimada", orden.fechaEntregaEstimada)
                }
                HorizontalDivider(Modifier.padding(vertical = 10.dp))
                Text("Motivo de ingreso", fontWeight = FontWeight.SemiBold)
                Text(orden.motivoIngreso, color = MaterialTheme.colorScheme.onSurfaceVariant)

                if (orden.observacionExterior.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text("Observación exterior", fontWeight = FontWeight.SemiBold)
                    Text(
                        orden.observacionExterior,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (orden.diagnosticoInicial.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text("Diagnóstico inicial y plazo", fontWeight = FontWeight.SemiBold)
                    Text(
                        orden.diagnosticoInicial,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            TarjetaInformacion("Inventario presente al ingreso") {
                if (orden.inventario.isEmpty()) {
                    Text(
                        "No se marcaron elementos.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        orden.inventario.joinToString(separator = "  •  "),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (orden.fotoUri != null) {
            item {
                Text("Evidencia de recepción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                AsyncImage(
                    model = orden.fotoUri,
                    contentDescription = "Fotografía del vehículo al ingresar al taller",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            if (orden.estado == EstadoOrden.ENTREGADO) {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Orden finalizada y entregada")
                }
            } else {
                Button(
                    onClick = onAvanzarEstado,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text("Avanzar a: ${orden.estado.siguiente().etiqueta}")
                }
            }
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun TarjetaInformacion(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            contenido()
        }
    }
}

@Composable
private fun FilaDato(
    icono: androidx.compose.ui.graphics.vector.ImageVector?,
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.size(9.dp))
        }
        Text(
            text = etiqueta,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            modifier = Modifier.weight(1.2f),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End
        )
    }
}
