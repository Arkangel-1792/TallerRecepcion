package com.luisbarragan.tallerrecepcion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import com.luisbarragan.tallerrecepcion.domain.repository.TallerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NuevaOrdenUiState(
    val cliente: String = "",
    val empresa: String = "",
    val email: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val placa: String = "",
    val marca: String = "",
    val modelo: String = "",
    val tipoVehiculo: String = "Automóvil",
    val color: String = "",
    val numeroSerie: String = "",
    val numeroMotor: String = "",
    val kilometraje: String = "",
    val tipoServicio: String = "Diagnóstico",
    val motivoIngreso: String = "",
    val observacionExterior: String = "",
    val nivelCombustible: String = "1/2",
    val fechaEntregaEstimada: String = "",
    val diagnosticoInicial: String = "",
    val inventario: Set<String> = emptySet(),
    val fotoUri: String? = null,
    val guardando: Boolean = false,
    val guardada: Boolean = false,
    val mensaje: String? = null
)

class NuevaOrdenViewModel(
    private val repository: TallerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NuevaOrdenUiState())
    val uiState: StateFlow<NuevaOrdenUiState> = _uiState.asStateFlow()

    fun actualizarCliente(valor: String) = cambiar { copy(cliente = valor) }
    fun actualizarEmpresa(valor: String) = cambiar { copy(empresa = valor) }
    fun actualizarEmail(valor: String) = cambiar { copy(email = valor) }
    fun actualizarDireccion(valor: String) = cambiar { copy(direccion = valor) }
    fun actualizarTelefono(valor: String) = cambiar { copy(telefono = valor) }
    fun actualizarPlaca(valor: String) = cambiar { copy(placa = valor.uppercase()) }
    fun actualizarMarca(valor: String) = cambiar { copy(marca = valor) }
    fun actualizarModelo(valor: String) = cambiar { copy(modelo = valor) }
    fun actualizarTipoVehiculo(valor: String) = cambiar { copy(tipoVehiculo = valor) }
    fun actualizarColor(valor: String) = cambiar { copy(color = valor) }
    fun actualizarNumeroSerie(valor: String) = cambiar { copy(numeroSerie = valor.uppercase()) }
    fun actualizarNumeroMotor(valor: String) = cambiar { copy(numeroMotor = valor.uppercase()) }
    fun actualizarKilometraje(valor: String) = cambiar { copy(kilometraje = valor.filter(Char::isDigit)) }
    fun actualizarTipoServicio(valor: String) = cambiar { copy(tipoServicio = valor) }
    fun actualizarMotivo(valor: String) = cambiar { copy(motivoIngreso = valor) }
    fun actualizarObservacion(valor: String) = cambiar { copy(observacionExterior = valor) }
    fun actualizarCombustible(valor: String) = cambiar { copy(nivelCombustible = valor) }
    fun actualizarFechaEntrega(valor: String) = cambiar { copy(fechaEntregaEstimada = valor) }
    fun actualizarDiagnostico(valor: String) = cambiar { copy(diagnosticoInicial = valor) }
    fun actualizarFoto(uri: String?) = cambiar { copy(fotoUri = uri) }

    fun alternarInventario(elemento: String) = cambiar {
        val nuevaSeleccion = if (elemento in inventario) inventario - elemento else inventario + elemento
        copy(inventario = nuevaSeleccion)
    }

    fun mostrarMensaje(texto: String) = cambiar { copy(mensaje = texto) }

    fun limpiarMensaje() = cambiar { copy(mensaje = null) }

    fun guardarOrden() {
        val datos = _uiState.value
        val kilometraje = datos.kilometraje.toIntOrNull()

        // Se valida antes de iniciar la corrutina para responder de inmediato en pantalla.
        val error = when {
            datos.cliente.isBlank() -> "Ingresa el nombre del cliente."
            datos.placa.isBlank() -> "Ingresa la placa o código del vehículo."
            datos.marca.isBlank() -> "Ingresa la marca del vehículo."
            kilometraje == null -> "Ingresa un kilometraje válido."
            datos.motivoIngreso.isBlank() -> "Describe el motivo de ingreso."
            else -> null
        }

        if (error != null) {
            mostrarMensaje(error)
            return
        }
        val kilometrajeValido = kilometraje ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, mensaje = null) }
            try {
                repository.guardarOrden(
                    OrdenTrabajo(
                        cliente = datos.cliente.trim(),
                        telefono = datos.telefono.trim(),
                        placa = datos.placa.trim(),
                        marca = datos.marca.trim(),
                        modelo = datos.modelo.trim(),
                        kilometraje = kilometrajeValido,
                        tipoServicio = datos.tipoServicio,
                        motivoIngreso = datos.motivoIngreso.trim(),
                        observacionExterior = datos.observacionExterior.trim(),
                        nivelCombustible = datos.nivelCombustible,
                        empresa = datos.empresa.trim(),
                        email = datos.email.trim(),
                        direccion = datos.direccion.trim(),
                        tipoVehiculo = datos.tipoVehiculo,
                        color = datos.color.trim(),
                        numeroSerie = datos.numeroSerie.trim(),
                        numeroMotor = datos.numeroMotor.trim(),
                        fechaEntregaEstimada = datos.fechaEntregaEstimada.trim(),
                        diagnosticoInicial = datos.diagnosticoInicial.trim(),
                        inventario = datos.inventario.sorted(),
                        fotoUri = datos.fotoUri
                    )
                )
                _uiState.update { it.copy(guardando = false, guardada = true) }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        guardando = false,
                        mensaje = "No se pudo guardar la recepción. Inténtalo nuevamente."
                    )
                }
            }
        }
    }

    private inline fun cambiar(transformacion: NuevaOrdenUiState.() -> NuevaOrdenUiState) {
        _uiState.update { estado -> estado.transformacion() }
    }

    companion object {
        fun factory(repository: TallerRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { NuevaOrdenViewModel(repository) }
        }
    }
}
