package com.luisbarragan.tallerrecepcion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisbarragan.tallerrecepcion.domain.model.MarcaVehiculo
import com.luisbarragan.tallerrecepcion.domain.repository.TallerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MarcasUiState(
    val cargando: Boolean = true,
    val marcas: List<MarcaVehiculo> = emptyList(),
    val filtro: String = "",
    val error: String? = null
) {
    val marcasVisibles: List<MarcaVehiculo>
        get() = if (filtro.isBlank()) marcas
        else marcas.filter { it.nombre.contains(filtro, ignoreCase = true) }
}

class MarcasViewModel(
    private val repository: TallerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MarcasUiState())
    val uiState: StateFlow<MarcasUiState> = _uiState.asStateFlow()

    init {
        cargarMarcas()
    }

    fun cargarMarcas() {
        viewModelScope.launch {
            // Reiniciar el error permite que el botón Reintentar muestre otra carga limpia.
            _uiState.update { it.copy(cargando = true, error = null) }
            try {
                val marcas = repository.consultarMarcas()
                _uiState.update { it.copy(cargando = false, marcas = marcas) }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        error = "No se pudieron consultar las marcas. Revisa tu conexión."
                    )
                }
            }
        }
    }

    fun actualizarFiltro(valor: String) {
        _uiState.update { it.copy(filtro = valor) }
    }

    companion object {
        fun factory(repository: TallerRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { MarcasViewModel(repository) }
        }
    }
}
