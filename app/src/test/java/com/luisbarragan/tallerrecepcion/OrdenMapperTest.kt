package com.luisbarragan.tallerrecepcion

import com.luisbarragan.tallerrecepcion.data.local.toDomain
import com.luisbarragan.tallerrecepcion.data.local.toEntity
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import org.junit.Assert.assertEquals
import org.junit.Test

class OrdenMapperTest {
    @Test
    fun `la orden conserva sus datos al pasar por Room`() {
        val orden = OrdenTrabajo(
            id = 12,
            cliente = "Cliente de Prueba",
            telefono = "0000000000",
            placa = "TEST-000",
            marca = "Toyota",
            modelo = "Hilux",
            kilometraje = 145000,
            tipoServicio = "Correctivo",
            motivoIngreso = "Ruido en suspensión",
            observacionExterior = "Rayón en guardafango derecho",
            nivelCombustible = "1/2",
            empresa = "Empresa Demo",
            email = "demo@example.invalid",
            direccion = "Dirección de prueba",
            tipoVehiculo = "Camioneta",
            color = "Blanco",
            numeroSerie = "SERIE-DEMO",
            numeroMotor = "MOTOR-DEMO",
            fechaEntregaEstimada = "26/08/2026",
            diagnosticoInicial = "Revisión en 24 horas",
            inventario = listOf("Gato", "Radio", "Llanta de repuesto"),
            estado = EstadoOrden.DIAGNOSTICO,
            fechaIngreso = 1_700_000_000_000,
            fotoUri = "content://foto/prueba"
        )

        assertEquals(orden, orden.toEntity().toDomain())
        assertEquals("OT-0012", orden.numeroOrden)
    }
}
