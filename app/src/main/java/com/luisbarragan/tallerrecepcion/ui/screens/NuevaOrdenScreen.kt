package com.luisbarragan.tallerrecepcion.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.luisbarragan.tallerrecepcion.ui.util.crearUriParaFoto
import com.luisbarragan.tallerrecepcion.ui.viewmodel.NuevaOrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaOrdenScreen(
    viewModel: NuevaOrdenViewModel,
    onVolver: () -> Unit,
    onConsultarMarcas: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { fotoTomada ->
        if (fotoTomada && uriPendiente != null) {
            viewModel.actualizarFoto(uriPendiente.toString())
        } else {
            viewModel.mostrarMensaje("No se tomó ninguna fotografía.")
        }
    }

    val abrirCamara = {
        runCatching { crearUriParaFoto(context) }
            .onSuccess { uri ->
                uriPendiente = uri
                camaraLauncher.launch(uri)
            }
            .onFailure {
                viewModel.mostrarMensaje("No se pudo abrir la cámara del dispositivo.")
            }
        Unit
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) abrirCamara()
        // La evidencia es útil, pero no debe bloquear el registro de la orden.
        else viewModel.mostrarMensaje("El permiso de cámara fue rechazado. Puedes continuar sin foto.")
    }

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let {
            snackbar.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    LaunchedEffect(state.guardada) {
        if (state.guardada) onVolver()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva recepción") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "Registra el ingreso por bloques. Los datos quedan guardados aunque cierres la aplicación.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SeccionFormulario(
                    titulo = "1. Datos del cliente",
                    icono = { Icon(Icons.Default.Person, contentDescription = null) }
                ) {
                    OutlinedTextField(
                        value = state.cliente,
                        onValueChange = viewModel::actualizarCliente,
                        label = { Text("Nombre del cliente *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.empresa,
                        onValueChange = viewModel::actualizarEmpresa,
                        label = { Text("Empresa") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::actualizarEmail,
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.telefono,
                        onValueChange = viewModel::actualizarTelefono,
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.direccion,
                        onValueChange = viewModel::actualizarDireccion,
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            item {
                SeccionFormulario(
                    titulo = "2. Vehículo",
                    icono = { Icon(Icons.Default.DirectionsCar, contentDescription = null) }
                ) {
                    OutlinedTextField(
                        value = state.placa,
                        onValueChange = viewModel::actualizarPlaca,
                        label = { Text("Placa o código *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.marca,
                        onValueChange = viewModel::actualizarMarca,
                        label = { Text("Marca *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    TextButton(onClick = onConsultarMarcas) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(Modifier.size(6.dp))
                        Text("Consultar catálogo público de marcas")
                    }
                    OutlinedTextField(
                        value = state.modelo,
                        onValueChange = viewModel::actualizarModelo,
                        label = { Text("Modelo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    EtiquetaCampo("Tipo de vehículo")
                    SelectorHorizontal(
                        opciones = listOf("Automóvil", "Camioneta", "SUV", "Otro"),
                        seleccionado = state.tipoVehiculo,
                        onSeleccionar = viewModel::actualizarTipoVehiculo
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.color,
                        onValueChange = viewModel::actualizarColor,
                        label = { Text("Color") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.numeroSerie,
                        onValueChange = viewModel::actualizarNumeroSerie,
                        label = { Text("Número de serie / VIN") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.numeroMotor,
                        onValueChange = viewModel::actualizarNumeroMotor,
                        label = { Text("Número de motor") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.kilometraje,
                        onValueChange = viewModel::actualizarKilometraje,
                        label = { Text("Kilometraje *") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        suffix = { Text("km") }
                    )
                }
            }

            item {
                SeccionFormulario(titulo = "3. Información de recepción") {
                    EtiquetaCampo("Tipo de servicio")
                    SelectorHorizontal(
                        opciones = listOf("Diagnóstico", "Preventivo", "Correctivo"),
                        seleccionado = state.tipoServicio,
                        onSeleccionar = viewModel::actualizarTipoServicio
                    )

                    Spacer(Modifier.height(12.dp))
                    EtiquetaCampo("Nivel de combustible")
                    SelectorHorizontal(
                        opciones = listOf("1/4", "1/2", "3/4", "Lleno"),
                        seleccionado = state.nivelCombustible,
                        onSeleccionar = viewModel::actualizarCombustible
                    )

                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.motivoIngreso,
                        onValueChange = viewModel::actualizarMotivo,
                        label = { Text("Motivo de ingreso *") },
                        placeholder = { Text("Ej.: ruido al frenar y revisión general") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.observacionExterior,
                        onValueChange = viewModel::actualizarObservacion,
                        label = { Text("Observación exterior") },
                        placeholder = { Text("Golpes, rayones u objetos dentro del vehículo") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.diagnosticoInicial,
                        onValueChange = viewModel::actualizarDiagnostico,
                        label = { Text("Diagnóstico inicial y plazo") },
                        placeholder = { Text("Ej.: revisar suspensión en 24 horas") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = state.fechaEntregaEstimada,
                        onValueChange = viewModel::actualizarFechaEntrega,
                        label = { Text("Entrega estimada") },
                        placeholder = { Text("DD/MM/AAAA - HH:MM") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            item {
                SeccionFormulario(
                    titulo = "4. Inventario del vehículo",
                    icono = { Icon(Icons.Default.Inventory2, contentDescription = null) }
                ) {
                    Text(
                        "Marca los elementos que están presentes al recibir el vehículo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    SelectorInventario(
                        titulo = "Exteriores",
                        opciones = inventarioExterior,
                        seleccionados = state.inventario,
                        onAlternar = viewModel::alternarInventario
                    )
                    Spacer(Modifier.height(12.dp))
                    SelectorInventario(
                        titulo = "Interiores",
                        opciones = inventarioInterior,
                        seleccionados = state.inventario,
                        onAlternar = viewModel::alternarInventario
                    )
                    Spacer(Modifier.height(12.dp))
                    SelectorInventario(
                        titulo = "Accesorios",
                        opciones = inventarioAccesorios,
                        seleccionados = state.inventario,
                        onAlternar = viewModel::alternarInventario
                    )
                    Spacer(Modifier.height(12.dp))
                    SelectorInventario(
                        titulo = "Componentes mecánicos",
                        opciones = inventarioMecanico,
                        seleccionados = state.inventario,
                        onAlternar = viewModel::alternarInventario
                    )
                }
            }

            item {
                SeccionFormulario(
                    titulo = "5. Evidencia fotográfica",
                    icono = { Icon(Icons.Default.CameraAlt, contentDescription = null) }
                ) {
                    if (state.fotoUri != null) {
                        AsyncImage(
                            model = state.fotoUri,
                            contentDescription = "Fotografía tomada durante la recepción",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(10.dp))
                    }

                    OutlinedButton(
                        onClick = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                abrirCamara()
                            } else {
                                permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(if (state.fotoUri == null) "Tomar fotografía" else "Repetir fotografía")
                    }
                    Text(
                        "La foto es opcional. Si niegas el permiso, el formulario sigue funcionando.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = viewModel::guardarOrden,
                    enabled = !state.guardando,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    if (state.guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Guardar recepción")
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SeccionFormulario(
    titulo: String,
    icono: (@Composable () -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                icono?.invoke()
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(14.dp))
            contenido()
        }
    }
}

@Composable
private fun EtiquetaCampo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun SelectorHorizontal(
    opciones: List<String>,
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = opcion == seleccionado,
                onClick = { onSeleccionar(opcion) },
                label = { Text(opcion) },
                leadingIcon = if (opcion == seleccionado) {
                    { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

@Composable
private fun SelectorInventario(
    titulo: String,
    opciones: List<String>,
    seleccionados: Set<String>,
    onAlternar: (String) -> Unit
) {
    EtiquetaCampo(titulo)
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opciones.forEach { opcion ->
            FilterChip(
                selected = opcion in seleccionados,
                onClick = { onAlternar(opcion) },
                label = { Text(opcion) },
                leadingIcon = if (opcion in seleccionados) {
                    { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

private val inventarioExterior = listOf(
    "Luces", "Antena", "Espejos", "Cristales", "Emblemas",
    "Llantas", "Tapa gasolina", "Limpiadores"
)

private val inventarioInterior = listOf(
    "Tablero", "Radio", "Encendedor", "Cinturones", "Manijas", "Tapetes"
)

private val inventarioAccesorios = listOf(
    "Gato", "Llave de ruedas", "Herramientas", "Triángulo",
    "Llanta de repuesto", "Extintor"
)

private val inventarioMecanico = listOf(
    "Claxon", "Tapa de aceite", "Tapa de radiador", "Varilla de aceite",
    "Filtro de aire", "Batería"
)
