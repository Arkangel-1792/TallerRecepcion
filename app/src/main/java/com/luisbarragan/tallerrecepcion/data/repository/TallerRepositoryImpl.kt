package com.luisbarragan.tallerrecepcion.data.repository

import com.luisbarragan.tallerrecepcion.data.local.OrdenTrabajoDao
import com.luisbarragan.tallerrecepcion.data.local.toDomain
import com.luisbarragan.tallerrecepcion.data.local.toEntity
import com.luisbarragan.tallerrecepcion.data.remote.VehiculosApi
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.MarcaVehiculo
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo
import com.luisbarragan.tallerrecepcion.domain.repository.TallerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TallerRepositoryImpl(
    private val dao: OrdenTrabajoDao,
    private val api: VehiculosApi
) : TallerRepository {

    override fun observarOrdenes(): Flow<List<OrdenTrabajo>> =
        dao.observarTodas().map { entidades -> entidades.map { it.toDomain() } }

    override fun observarOrden(id: Long): Flow<OrdenTrabajo?> =
        dao.observarPorId(id).map { it?.toDomain() }

    override suspend fun guardarOrden(orden: OrdenTrabajo): Long =
        dao.insertar(orden.toEntity())

    override suspend fun actualizarEstado(id: Long, estado: EstadoOrden) {
        dao.actualizarEstado(id, estado.name)
    }

    override suspend fun eliminarOrden(orden: OrdenTrabajo) {
        dao.eliminar(orden.toEntity())
    }

    override suspend fun consultarMarcas(): List<MarcaVehiculo> =
        api.obtenerMarcas().resultados
            // La API puede devolver la misma marca para más de una categoría.
            .distinctBy { it.id }
            .map { MarcaVehiculo(it.id, it.nombre.trim(), it.tipoVehiculo) }
            .sortedBy { it.nombre }
}
