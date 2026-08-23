package com.luisbarragan.tallerrecepcion.domain.model

enum class EstadoOrden(val etiqueta: String) {
    RECEPCION("Recepción"),
    DIAGNOSTICO("Diagnóstico"),
    EN_PROCESO("En proceso"),
    LISTO("Listo"),
    ENTREGADO("Entregado");

    fun siguiente(): EstadoOrden = when (this) {
        RECEPCION -> DIAGNOSTICO
        DIAGNOSTICO -> EN_PROCESO
        EN_PROCESO -> LISTO
        LISTO -> ENTREGADO
        ENTREGADO -> ENTREGADO
    }
}
