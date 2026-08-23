package com.luisbarragan.tallerrecepcion.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface VehiculosApi {
    @GET("vehicles/GetMakesForVehicleType/car")
    suspend fun obtenerMarcas(
        @Query("format") formato: String = "json"
    ): MarcasResponse
}
