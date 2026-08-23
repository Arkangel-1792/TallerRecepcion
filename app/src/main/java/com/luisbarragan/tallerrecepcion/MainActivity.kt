package com.luisbarragan.tallerrecepcion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luisbarragan.tallerrecepcion.ui.navigation.TallerNavHost
import com.luisbarragan.tallerrecepcion.ui.theme.TallerRecepcionTheme
import com.luisbarragan.tallerrecepcion.ui.viewmodel.AjustesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val app = application as TallerRecepcionApplication
            val ajustesViewModel: AjustesViewModel = viewModel(
                factory = AjustesViewModel.factory(app.container.ajustesRepository)
            )
            val modoOscuro by ajustesViewModel.modoOscuro.collectAsStateWithLifecycle()

            TallerRecepcionTheme(darkTheme = modoOscuro) {
                Surface {
                    TallerNavHost(
                        container = app.container,
                        modoOscuro = modoOscuro,
                        onCambiarModoOscuro = ajustesViewModel::cambiarModoOscuro
                    )
                }
            }
        }
    }
}
