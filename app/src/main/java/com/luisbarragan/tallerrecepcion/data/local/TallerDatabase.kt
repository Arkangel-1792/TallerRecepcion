package com.luisbarragan.tallerrecepcion.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [OrdenTrabajoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TallerDatabase : RoomDatabase() {
    abstract fun ordenTrabajoDao(): OrdenTrabajoDao

    companion object {
        @Volatile
        private var instance: TallerDatabase? = null

        // Una sola instancia evita abrir varias conexiones a la misma base.
        fun getInstance(context: Context): TallerDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TallerDatabase::class.java,
                    "taller_recepcion.db"
                ).build().also { instance = it }
            }
    }
}
