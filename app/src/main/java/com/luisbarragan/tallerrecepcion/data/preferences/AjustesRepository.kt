package com.luisbarragan.tallerrecepcion.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "ajustes_taller")

class AjustesRepository(private val context: Context) {
    private val claveModoOscuro = booleanPreferencesKey("modo_oscuro")

    val modoOscuro: Flow<Boolean> = context.dataStore.data
        .catch { error ->
            if (error is IOException) emit(androidx.datastore.preferences.core.emptyPreferences())
            else throw error
        }
        .map { preferencias -> preferencias[claveModoOscuro] ?: false }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[claveModoOscuro] = activado
        }
    }
}
