package com.luisbarragan.tallerrecepcion.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenTrabajoDao {
    @Query("SELECT * FROM ordenes_trabajo ORDER BY fechaIngreso DESC")
    fun observarTodas(): Flow<List<OrdenTrabajoEntity>>

    @Query("SELECT * FROM ordenes_trabajo WHERE id = :id")
    fun observarPorId(id: Long): Flow<OrdenTrabajoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(orden: OrdenTrabajoEntity): Long

    @Query("UPDATE ordenes_trabajo SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Long, estado: String)

    @Delete
    suspend fun eliminar(orden: OrdenTrabajoEntity)
}
