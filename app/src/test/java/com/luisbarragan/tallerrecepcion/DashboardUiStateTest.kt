package com.luisbarragan.tallerrecepcion

import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import com.luisbarragan.tallerrecepcion.ui.viewmodel.DashboardUiState
import com.luisbarragan.tallerrecepcion.ui.viewmodel.ordenesVisibles
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardUiStateTest {
    private val orden = OrdenTrabajo(
        id = 7,
        cliente = "Cliente Demo",
        telefono = "",
        placa = "ABC-0000",
        marca = "Toyota",
        modelo = "Corolla",
        kilometraje = 82000,
        tipoServicio = "Preventivo",
        motivoIngreso = "Mantenimiento",
        observacionExterior = "",
        nivelCombustible = "1/2"
    )

    @Test
    fun `la busqueda encuentra por cliente placa y numero de orden`() {
        val estado = DashboardUiState(ordenes = listOf(orden))

        assertEquals(1, estado.copy(filtro = "demo").ordenesVisibles.size)
        assertEquals(1, estado.copy(filtro = "abc").ordenesVisibles.size)
        assertEquals(1, estado.copy(filtro = "OT-0007").ordenesVisibles.size)
        assertEquals(0, estado.copy(filtro = "Honda").ordenesVisibles.size)
    }
}
