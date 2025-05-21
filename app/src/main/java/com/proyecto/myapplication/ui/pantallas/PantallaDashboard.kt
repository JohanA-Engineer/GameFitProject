package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.viewmodel.UserViewModel

@Composable
fun PantallaDashboard(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFCC80), Color(0xFFCE93D8))
    )

    userViewModel.calcularMetaCalorica()

    val diasTotales = userViewModel.tiempoEstimadoDias.value
    val diasTranscurridos = userViewModel.diasCompletados.value
    val progreso = if (diasTotales > 0) diasTranscurridos.toFloat() / diasTotales else 0f

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoGradiente)
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Progreso",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = progreso,
                strokeWidth = 12.dp,
                color = Color(0xFF8E24AA),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f) // mantiene proporción cuadrada
            )
            Text(
                text = "$diasTranscurridos / $diasTotales días",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Energía que usas cada día: ${userViewModel.caloriasQuemadas.value}",
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lo que debes comer cada día para tu meta: ${userViewModel.caloriasDiarias.value}",
            fontSize = 20.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { navController.navigate("rutina") },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
        ) {
            Text("Ir a Rutina", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("menu") },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E24AA))
        ) {
            Text("Volver al menú", color = Color.White, fontSize = 18.sp)
        }
    }
}
