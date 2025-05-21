package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.R

@Composable
fun PantallaMenu(navController: NavHostController) {
    val gradientColors = listOf(
        Color(0xAAFFD180), // Naranja claro semi-transparente
        Color(0xAACE93D8)  // Morado suave semi-transparente
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Capa de gradiente semitransparente sobre toda la pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(gradientColors))
        )

        // Contenido del menú
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding() // Respetar el notch y la barra de estado
                .padding(24.dp),
            verticalArrangement = Arrangement.Center, // Centramos los elementos en el centro de la pantalla
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espacio adicional para mover la imagen hacia abajo
            Spacer(modifier = Modifier.height(32.dp))

            // Imagen justo arriba del primer botón
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Ajuste en altura para la imagen
                contentAlignment = Alignment.Center // Centrar la imagen dentro del Box
            ) {
                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Fondo del menú",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Altura de la imagen
                    contentScale = ContentScale.Fit // Ajusta la imagen para que sea completamente visible
                )
            }

            // Botón "Mi Progreso"
            Button(
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mi Progreso", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Recompensas"
            Button(
                onClick = { navController.navigate("recompensas") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Recompensas", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Datos Personales"
            Button(
                onClick = { navController.navigate("datos") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Datos Personales", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Mi Usuario"
            Button(
                onClick = { navController.navigate("usuario") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mi Usuario", fontSize = 18.sp)
            }
        }
    }
}


