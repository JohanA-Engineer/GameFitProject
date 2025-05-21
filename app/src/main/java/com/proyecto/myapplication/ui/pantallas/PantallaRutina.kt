package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.viewmodel.MonedasViewModel
import com.proyecto.myapplication.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import com.proyecto.myapplication.data.RutinaDataStore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaRutina(
    navController: NavHostController,
    monedasViewModel: MonedasViewModel,
    userViewModel: UserViewModel
) {
    val rutina = listOf(
        "Calentamiento: 5 minutos de saltos",
        "Sentadillas: 3 series de 15 repeticiones",
        "Flexiones: 3 series de 10 repeticiones",
        "Abdominales: 3 series de 20 repeticiones",
        "Burpees: 3 series de 10 repeticiones",
        "Estiramientos: 5 minutos"
    )

    val monedas by monedasViewModel.monedas.collectAsState()
    val fondoGradiente = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFCC80), Color(0xFFCE93D8))
    )

    val context = LocalContext.current
    val rutinaDataStore = remember { RutinaDataStore(context) }
    val scope = rememberCoroutineScope()

    val completados = rutina.map { ejercicio ->
        rutinaDataStore.estaCompletadoHoy(ejercicio).collectAsState(initial = false)
    }

    val rutinaCompletada = completados.all { it.value }
    val rutinaYaCompletadaHoy by rutinaDataStore.rutinaCompletadaHoy().collectAsState(initial = false)

    LaunchedEffect(rutinaCompletada) {
        if (rutinaCompletada && !rutinaYaCompletadaHoy) {
            userViewModel.incrementarDiasCompletados()
            val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            rutinaDataStore.guardarRutinaCompletadaHoy(fechaHoy)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(fondoGradiente)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Reto de Hoy",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        item {
            Text(
                text = "💪 \"Cada pequeño paso te acerca a tu meta.\" 💪",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }

        item {
            Text(
                text = "Monedas: $monedas",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Días completados: ${userViewModel.diasCompletados.value}",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(rutina) { ejercicio ->
            val completadoHoy by rutinaDataStore.estaCompletadoHoy(ejercicio).collectAsState(initial = false)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = ejercicio, style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                monedasViewModel.agregarMonedas(5)
                                rutinaDataStore.guardarFecha(ejercicio)
                            }
                        },
                        enabled = !completadoHoy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (completadoHoy) "Completado" else "+5")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Volver")
                }
            }
        }
    }
}
