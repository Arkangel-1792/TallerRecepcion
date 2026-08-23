package com.luisbarragan.tallerrecepcion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import com.luisbarragan.tallerrecepcion.domain.repository.TallerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val ordenes: List<OrdenTrabajo> = emptyList(),
    val cantidades: Map<EstadoOrden, Int> = EstadoOrden.entries.associateWith { 0 },
    val filtro: String = ""
)

val DashboardUiState.ordenesVisibles: List<OrdenTrabajo>
    get() {
        val texto = filtro.trim()
        if (texto.isBlank()) return ordenes

        return ordenes.filter { orden ->
            orden.numeroOrden.contains(texto, ignoreCase = true) ||
                orden.cliente.contains(texto, ignoreCase = true) ||
                orden.placa.contains(texto, ignoreCase = true) ||
                orden.marca.contains(texto, ignoreCase = true) ||
                orden.modelo.contains(texto, ignoreCase = true)
        }
    }

class DashboardViewModel(
    repository: TallerRepository
) : ViewModel() {
    private val filtro = MutableStateFlow("")

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.observarOrdenes(),
        filtro
    ) { ordenes, textoFiltro ->
            DashboardUiState(
                ordenes = ordenes,
                cantidades = EstadoOrden.entries.associateWith { estado ->
                    ordenes.count { it.estado == estado }
                },
                filtro = textoFiltro
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )

    fun actualizarFiltro(valor: String) {
        filtro.value = valor
    }

    companion object {
        fun factory(repository: TallerRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { DashboardViewModel(repository) }
        }
    }
}
