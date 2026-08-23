package com.luisbarragan.tallerrecepcion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisbarragan.tallerrecepcion.data.preferences.AjustesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AjustesViewModel(
    private val repository: AjustesRepository
) : ViewModel() {

    val modoOscuro: StateFlow<Boolean> = repository.modoOscuro.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            repository.guardarModoOscuro(activado)
        }
    }

    companion object {
        fun factory(repository: AjustesRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { AjustesViewModel(repository) }
        }
    }
}
