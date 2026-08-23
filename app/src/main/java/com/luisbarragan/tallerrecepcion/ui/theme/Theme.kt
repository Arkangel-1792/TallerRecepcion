package com.luisbarragan.tallerrecepcion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AzulTaller,
    onPrimary = Color.White,
    primaryContainer = AzulTallerClaro,
    onPrimaryContainer = AzulTaller,
    secondary = VerdeAccion,
    onSecondary = Color.White,
    secondaryContainer = VerdeClaro,
    onSecondaryContainer = Color(0xFF0C432C),
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = Color.White,
    onSurface = TextoOscuro
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA8CBE8),
    onPrimary = Color(0xFF0E304C),
    primaryContainer = Color(0xFF264A67),
    onPrimaryContainer = Color(0xFFD4E8F8),
    secondary = Color(0xFF86D5AA),
    onSecondary = Color(0xFF073821),
    secondaryContainer = Color(0xFF155238),
    onSecondaryContainer = Color(0xFFA3F2C5),
    background = FondoOscuro,
    onBackground = Color(0xFFE0E6EA),
    surface = SuperficieOscura,
    onSurface = Color(0xFFE0E6EA)
)

@Composable
fun TallerRecepcionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
