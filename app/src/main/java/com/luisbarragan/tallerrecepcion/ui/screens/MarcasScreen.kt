package com.luisbarragan.tallerrecepcion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.luisbarragan.tallerrecepcion.domain.model.MarcaVehiculo
import com.luisbarragan.tallerrecepcion.ui.viewmodel.MarcasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarcasScreen(
    viewModel: MarcasViewModel,
    onVolver: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de marcas") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Datos consultados en tiempo real desde la API pública vPIC de NHTSA.",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = state.filtro,
                onValueChange = viewModel::actualizarFiltro,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Buscar marca") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            when {
                state.cargando -> CargandoMarcas()
                state.error != null -> ErrorMarcas(
                    mensaje = state.error ?: "Error de conexión",
                    onReintentar = viewModel::cargarMarcas
                )
                else -> ListaMarcas(state.marcasVisibles)
            }
        }
    }
}

@Composable
private fun CargandoMarcas() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.size(12.dp))
            Text("Consultando marcas...")
        }
    }
}

@Composable
private fun ErrorMarcas(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(52.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.size(12.dp))
            Text(mensaje, style = MaterialTheme.typography.bodyLarge)
            TextButton(onClick = onReintentar) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.size(6.dp))
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun ListaMarcas(marcas: List<MarcaVehiculo>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (marcas.isEmpty()) {
            item {
                Text(
                    "No se encontraron coincidencias.",
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }

        items(marcas, key = { it.id }) { marca ->
            Card(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Text(
                            text = marca.nombre,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = marca.tipoVehiculo,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
