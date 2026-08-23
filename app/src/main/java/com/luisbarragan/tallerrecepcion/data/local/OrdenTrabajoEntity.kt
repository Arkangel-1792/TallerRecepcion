package com.luisbarragan.tallerrecepcion.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden
import com.luisbarragan.tallerrecepcion.domain.model.OrdenTrabajo

@Entity(tableName = "ordenes_trabajo")
data class OrdenTrabajoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cliente: String,
    val telefono: String,
    val placa: String,
    val marca: String,
    val modelo: String,
    val kilometraje: Int,
    val tipoServicio: String,
    val motivoIngreso: String,
    val observacionExterior: String,
    val nivelCombustible: String,
    val empresa: String,
    val email: String,
    val direccion: String,
    val tipoVehiculo: String,
    val color: String,
    val numeroSerie: String,
    val numeroMotor: String,
    val fechaEntregaEstimada: String,
    val diagnosticoInicial: String,
    val inventario: String,
    val estado: String,
    val fechaIngreso: Long,
    val fotoUri: String?
)

fun OrdenTrabajoEntity.toDomain(): OrdenTrabajo = OrdenTrabajo(
    id = id,
    cliente = cliente,
    telefono = telefono,
    placa = placa,
    marca = marca,
    modelo = modelo,
    kilometraje = kilometraje,
    tipoServicio = tipoServicio,
    motivoIngreso = motivoIngreso,
    observacionExterior = observacionExterior,
    nivelCombustible = nivelCombustible,
    empresa = empresa,
    email = email,
    direccion = direccion,
    tipoVehiculo = tipoVehiculo,
    color = color,
    numeroSerie = numeroSerie,
    numeroMotor = numeroMotor,
    fechaEntregaEstimada = fechaEntregaEstimada,
    diagnosticoInicial = diagnosticoInicial,
    inventario = inventario.split("|").filter { it.isNotBlank() },
    // Si aparece un valor antiguo o inválido, la orden vuelve al estado inicial.
    estado = runCatching { EstadoOrden.valueOf(estado) }.getOrDefault(EstadoOrden.RECEPCION),
    fechaIngreso = fechaIngreso,
    fotoUri = fotoUri
)

fun OrdenTrabajo.toEntity(): OrdenTrabajoEntity = OrdenTrabajoEntity(
    id = id,
    cliente = cliente,
    telefono = telefono,
    placa = placa,
    marca = marca,
    modelo = modelo,
    kilometraje = kilometraje,
    tipoServicio = tipoServicio,
    motivoIngreso = motivoIngreso,
    observacionExterior = observacionExterior,
    nivelCombustible = nivelCombustible,
    empresa = empresa,
    email = email,
    direccion = direccion,
    tipoVehiculo = tipoVehiculo,
    color = color,
    numeroSerie = numeroSerie,
    numeroMotor = numeroMotor,
    fechaEntregaEstimada = fechaEntregaEstimada,
    diagnosticoInicial = diagnosticoInicial,
    inventario = inventario.joinToString("|"),
    estado = estado.name,
    fechaIngreso = fechaIngreso,
    fotoUri = fotoUri
)
