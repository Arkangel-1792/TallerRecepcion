package com.luisbarragan.tallerrecepcion.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luisbarragan.tallerrecepcion.di.AppContainer
import com.luisbarragan.tallerrecepcion.ui.screens.AjustesScreen
import com.luisbarragan.tallerrecepcion.ui.screens.DashboardScreen
import com.luisbarragan.tallerrecepcion.ui.screens.DetalleOrdenScreen
import com.luisbarragan.tallerrecepcion.ui.screens.MarcasScreen
import com.luisbarragan.tallerrecepcion.ui.screens.NuevaOrdenScreen
import com.luisbarragan.tallerrecepcion.ui.viewmodel.DashboardViewModel
import com.luisbarragan.tallerrecepcion.ui.viewmodel.DetalleOrdenViewModel
import com.luisbarragan.tallerrecepcion.ui.viewmodel.MarcasViewModel
import com.luisbarragan.tallerrecepcion.ui.viewmodel.NuevaOrdenViewModel

private data class OpcionNavegacion(
    val ruta: String,
    val texto: String,
    val icono: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun TallerNavHost(
    container: AppContainer,
    modoOscuro: Boolean,
    onCambiarModoOscuro: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val opciones = listOf(
        OpcionNavegacion(Rutas.INICIO, "Inicio", Icons.Default.Home),
        OpcionNavegacion(Rutas.MARCAS, "Marcas", Icons.Default.DirectionsCar),
        OpcionNavegacion(Rutas.AJUSTES, "Ajustes", Icons.Default.Settings)
    )
    // Las pantallas secundarias usan flecha de regreso y no necesitan la barra inferior.
    val mostrarBarra = rutaActual in opciones.map { it.ruta }

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar {
                    opciones.forEach { opcion ->
                        NavigationBarItem(
                            selected = rutaActual == opcion.ruta,
                            onClick = {
                                navController.navigate(opcion.ruta) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(opcion.icono, contentDescription = opcion.texto)
                            },
                            label = { Text(opcion.texto) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.INICIO) {
                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModel.factory(container.tallerRepository)
                )
                val uiState by dashboardViewModel.uiState.collectAsStateWithLifecycle()

                DashboardScreen(
                    uiState = uiState,
                    onFiltroCambiado = dashboardViewModel::actualizarFiltro,
                    onNuevaRecepcion = { navController.navigate(Rutas.NUEVA_ORDEN) },
                    onAbrirOrden = { id -> navController.navigate(Rutas.detalle(id)) }
                )
            }

            composable(Rutas.NUEVA_ORDEN) {
                val nuevaOrdenViewModel: NuevaOrdenViewModel = viewModel(
                    factory = NuevaOrdenViewModel.factory(container.tallerRepository)
                )
                NuevaOrdenScreen(
                    viewModel = nuevaOrdenViewModel,
                    onVolver = { navController.popBackStack() },
                    onConsultarMarcas = { navController.navigate(Rutas.MARCAS) }
                )
            }

            composable(
                route = Rutas.DETALLE,
                arguments = listOf(navArgument("ordenId") { type = NavType.LongType })
            ) { entry ->
                val ordenId = entry.arguments?.getLong("ordenId") ?: 0L
                val detalleViewModel: DetalleOrdenViewModel = viewModel(
                    factory = DetalleOrdenViewModel.factory(
                        ordenId = ordenId,
                        repository = container.tallerRepository
                    )
                )
                DetalleOrdenScreen(
                    viewModel = detalleViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Rutas.MARCAS) {
                val marcasViewModel: MarcasViewModel = viewModel(
                    factory = MarcasViewModel.factory(container.tallerRepository)
                )
                MarcasScreen(
                    viewModel = marcasViewModel,
                    onVolver = { navController.popBackStack() }
                )
            }

            composable(Rutas.AJUSTES) {
                AjustesScreen(
                    modoOscuro = modoOscuro,
                    onCambiarModoOscuro = onCambiarModoOscuro
                )
            }
        }
    }
}
