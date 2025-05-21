package com.proyecto.myapplication.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.myapplication.ui.pantallas.*
import com.proyecto.myapplication.viewmodel.UserViewModel
import com.proyecto.myapplication.viewmodel.MonedasViewModel


@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel,
    monedasViewModel: MonedasViewModel
) {
    NavHost(navController = navController, startDestination = "bienvenida") {

        composable("inicioSesion") { PantallaInicioSesion(navController) }
        composable("bienvenida") { PantallaBienvenida(navController) }
        composable("registro") { PantallaRegistro(navController, userViewModel) }
        composable("datos") { PantallaDatosPersonales(navController, userViewModel) }
        composable("dashboard") { PantallaDashboard(navController, userViewModel) }
        composable("recompensas") { PantallaRecompensas(navController, monedasViewModel, userViewModel) }
        composable("menu") { PantallaMenu( navController) }
        composable("usuario") { PantallaUsuario(navController, userViewModel) }
        composable("rutina") { PantallaRutina(navController, monedasViewModel, userViewModel) }
        composable("juego") { PantallaJuego(navController, monedasViewModel) }
        composable("tienda_personajes") { PantallaTiendaPersonajes(navController, monedasViewModel) }
    }
}