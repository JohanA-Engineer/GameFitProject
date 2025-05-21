package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.R
import com.proyecto.myapplication.viewmodel.MonedasViewModel
import kotlinx.coroutines.launch

data class Personaje(val nombre: String, val precio: Int, val imagenId: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTiendaPersonajes(
    navController: NavHostController,
    monedasViewModel: MonedasViewModel
) {
    val personajes = listOf(
        Personaje("Guerrero", 200, R.drawable.personaje1),
        Personaje("Explorador", 300, R.drawable.personaje2),
        Personaje("Mago", 400, R.drawable.personaje3)
    )

    val monedas by monedasViewModel.monedas.collectAsState()
    val colors = listOf(Color(0xFFFFD180), Color(0xFFCE93D8))

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estado local de personajes comprados
    var personajesComprados by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tienda de Personajes") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors))
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    "Monedas disponibles: $monedas",
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(personajes.size) { index ->
                        val personaje = personajes[index]
                        val comprado = personaje.nombre in personajesComprados

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !comprado) {
                                    if (monedas >= personaje.precio) {
                                        monedasViewModel.restarMonedas(personaje.precio)
                                        personajesComprados = personajesComprados + personaje.nombre
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("No tienes sufficientes monedas.")
                                        }
                                    }
                                },
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (comprado) Color.LightGray else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = personaje.imagenId),
                                    contentDescription = personaje.nombre,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        personaje.nombre,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        if (comprado) "Comprado"
                                        else "Precio: ${personaje.precio} monedas"
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Volver")
                }
            }
        }
    }
}
