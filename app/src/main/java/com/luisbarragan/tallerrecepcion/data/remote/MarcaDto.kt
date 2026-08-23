package com.luisbarragan.tallerrecepcion.data.remote

import com.google.gson.annotations.SerializedName

data class MarcasResponse(
    @SerializedName("Results")
    val resultados: List<MarcaDto> = emptyList()
)

data class MarcaDto(
    @SerializedName("MakeId")
    val id: Int,
    @SerializedName("MakeName")
    val nombre: String,
    @SerializedName("VehicleTypeName")
    val tipoVehiculo: String
)
