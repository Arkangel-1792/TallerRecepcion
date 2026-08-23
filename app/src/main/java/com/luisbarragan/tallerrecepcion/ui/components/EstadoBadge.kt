package com.luisbarragan.tallerrecepcion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luisbarragan.tallerrecepcion.domain.model.EstadoOrden

@Composable
fun EstadoBadge(
    estado: EstadoOrden,
    modifier: Modifier = Modifier
) {
    val color = when (estado) {
        EstadoOrden.RECEPCION -> Color(0xFF2D6EA3)
        EstadoOrden.DIAGNOSTICO -> Color(0xFF8A5A00)
        EstadoOrden.EN_PROCESO -> Color(0xFF7A4AA8)
        EstadoOrden.LISTO -> Color(0xFF247A4D)
        EstadoOrden.ENTREGADO -> Color(0xFF59636D)
    }

    Text(
        text = estado.etiqueta,
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        color = color,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold
    )
}
