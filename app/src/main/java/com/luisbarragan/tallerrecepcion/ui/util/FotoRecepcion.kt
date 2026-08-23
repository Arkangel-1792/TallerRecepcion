package com.luisbarragan.tallerrecepcion.ui.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun crearUriParaFoto(context: Context): Uri {
    // La imagen queda dentro de filesDir; FileProvider entrega una URI temporal segura.
    val carpeta = File(context.filesDir, "fotos_recepcion").apply { mkdirs() }
    val fecha = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val archivo = File.createTempFile("recepcion_$fecha", ".jpg", carpeta)

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}
