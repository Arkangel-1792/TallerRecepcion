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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetalleOrdenViewModel(
    ordenId: Long,
    private val repository: TallerRepository
) : ViewModel() {

    val orden: StateFlow<OrdenTrabajo?> = repository.observarOrden(ordenId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _eliminada = MutableStateFlow(false)
    val eliminada: StateFlow<Boolean> = _eliminada.asStateFlow()

    fun avanzarEstado() {
        val ordenActual = orden.value ?: return
        val siguiente = ordenActual.estado.siguiente()
        if (ordenActual.estado == EstadoOrden.ENTREGADO) return

        viewModelScope.launch {
            repository.actualizarEstado(ordenActual.id, siguiente)
        }
    }

    fun eliminarOrden() {
        val ordenActual = orden.value ?: return
        viewModelScope.launch {
            repository.eliminarOrden(ordenActual)
            _eliminada.value = true
        }
    }

    companion object {
        fun factory(
            ordenId: Long,
            repository: TallerRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer { DetalleOrdenViewModel(ordenId, repository) }
        }
    }
}
