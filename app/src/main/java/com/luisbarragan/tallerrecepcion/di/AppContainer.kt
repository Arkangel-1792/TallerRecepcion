package com.luisbarragan.tallerrecepcion.di

import android.content.Context
import com.luisbarragan.tallerrecepcion.data.local.TallerDatabase
import com.luisbarragan.tallerrecepcion.data.preferences.AjustesRepository
import com.luisbarragan.tallerrecepcion.data.remote.VehiculosApi
import com.luisbarragan.tallerrecepcion.data.repository.TallerRepositoryImpl
import com.luisbarragan.tallerrecepcion.domain.repository.TallerRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private val database = TallerDatabase.getInstance(context)

    private val vehiculosApi: VehiculosApi = Retrofit.Builder()
        .baseUrl("https://vpic.nhtsa.dot.gov/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VehiculosApi::class.java)

    val tallerRepository: TallerRepository = TallerRepositoryImpl(
        dao = database.ordenTrabajoDao(),
        api = vehiculosApi
    )

    val ajustesRepository = AjustesRepository(context)
}
