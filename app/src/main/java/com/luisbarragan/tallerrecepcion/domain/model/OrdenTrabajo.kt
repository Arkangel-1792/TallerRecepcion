package com.luisbarragan.tallerrecepcion.domain.model

data class OrdenTrabajo(
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
    val empresa: String = "",
    val email: String = "",
    val direccion: String = "",
    val tipoVehiculo: String = "",
    val color: String = "",
    val numeroSerie: String = "",
    val numeroMotor: String = "",
    val fechaEntregaEstimada: String = "",
    val diagnosticoInicial: String = "",
    val inventario: List<String> = emptyList(),
    val estado: EstadoOrden = EstadoOrden.RECEPCION,
    val fechaIngreso: Long = System.currentTimeMillis(),
    val fotoUri: String? = null
) {
    val numeroOrden: String
        get() = "OT-${id.toString().padStart(4, '0')}"
}
