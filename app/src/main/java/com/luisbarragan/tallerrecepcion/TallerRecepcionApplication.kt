package com.luisbarragan.tallerrecepcion

import android.app.Application
import com.luisbarragan.tallerrecepcion.di.AppContainer

class TallerRecepcionApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
