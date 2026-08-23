package com.luisbarragan.tallerrecepcion.ui.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.comoFecha(): String =
    SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("es", "EC")).format(Date(this))

fun Int.comoKilometraje(): String =
    NumberFormat.getIntegerInstance(Locale("es", "EC")).format(this) + " km"
