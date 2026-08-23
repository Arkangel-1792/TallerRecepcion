package com.luisbarragan.tallerrecepcion

import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import org.junit.Assert.assertEquals
import org.junit.Test

class EstadoOrdenTest {
    @Test
    fun `la orden avanza por el flujo esperado`() {
        assertEquals(EstadoOrden.DIAGNOSTICO, EstadoOrden.RECEPCION.siguiente())
        assertEquals(EstadoOrden.EN_PROCESO, EstadoOrden.DIAGNOSTICO.siguiente())
        assertEquals(EstadoOrden.LISTO, EstadoOrden.EN_PROCESO.siguiente())
        assertEquals(EstadoOrden.ENTREGADO, EstadoOrden.LISTO.siguiente())
        assertEquals(EstadoOrden.ENTREGADO, EstadoOrden.ENTREGADO.siguiente())
    }
}
