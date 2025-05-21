package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.viewmodel.MonedasViewModel
import com.proyecto.myapplication.viewmodel.UserViewModel

@Composable
fun PantallaRecompensas(
    navController: NavHostController,
    monedasViewModel: MonedasViewModel,
    userViewModel: UserViewModel
) {
    val monedas by monedasViewModel.monedas.collectAsState()
    var recompensaReclamada by remember { mutableStateOf(false) }

    val rutinaCompletada = userViewModel.diasCompletados.value > 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFD180), // Naranja claro
                        Color(0xFFCE93D8)  // Morado claro
                    )
                )
            )
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Recompensas", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Monedas",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Monedas: $monedas", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!recompensaReclamada) {
                        monedasViewModel.agregarMonedas(50)
                        recompensaReclamada = true
                    }
                },
                enabled = !recompensaReclamada
            ) {
                Text(
                    if (!recompensaReclamada)
                        "Reclamar recompensa diaria 🎁"
                    else
                        "Recompensa ya reclamada"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("juego") },
                enabled = rutinaCompletada
            ) {
                Text("Jugar y ganar más monedas 🎮")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                navController.navigate("tienda_personajes")
            }) {
                Text("Comprar personajes 🛒")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                navController.navigate("menu")
            }) {
                Text("Volver al menú")
            }
        }
    }
}

