package com.luisbarragan.tallerrecepcion.ui.navigation

object Rutas {
    const val INICIO = "inicio"
    const val NUEVA_ORDEN = "nueva_orden"
    const val MARCAS = "marcas"
    const val AJUSTES = "ajustes"
    const val DETALLE = "detalle/{ordenId}"

    fun detalle(ordenId: Long): String = "detalle/$ordenId"
}
