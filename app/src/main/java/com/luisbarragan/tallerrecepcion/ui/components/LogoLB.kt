package com.luisbarragan.tallerrecepcion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LogoLB(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "LB",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black
        )
    }
}
