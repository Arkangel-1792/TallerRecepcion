package com.luisbarragan.tallerrecepcion.domain.repository

import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.MarcaVehiculo
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import kotlinx.coroutines.flow.Flow

interface TallerRepository {
    fun observarOrdenes(): Flow<List<OrdenTrabajo>>

    fun observarOrden(id: Long): Flow<OrdenTrabajo?>

    suspend fun guardarOrden(orden: OrdenTrabajo): Long

    suspend fun actualizarEstado(id: Long, estado: EstadoOrden)

    suspend fun eliminarOrden(orden: OrdenTrabajo)

    suspend fun consultarMarcas(): List<MarcaVehiculo>
}
