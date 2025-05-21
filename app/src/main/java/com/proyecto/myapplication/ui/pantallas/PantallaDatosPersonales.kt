package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDatosPersonales(navController: NavHostController, userViewModel: UserViewModel) {
    var nombre by remember { userViewModel.nombre }
    var edad by remember { userViewModel.edad }
    var sexo by remember { userViewModel.sexo }
    var estatura by remember { userViewModel.estatura }
    var peso by remember { userViewModel.peso }
    var pesoMeta by remember { userViewModel.pesoMeta }

    var nivelActividad by remember { mutableStateOf("Moderado") }
    var expandedSexo by remember { mutableStateOf(false) }
    var expandedActividad by remember { mutableStateOf(false) }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var resultadoCalorias by remember { mutableStateOf("") }
    var caloriasPerdidasDiarias by remember { mutableStateOf("") }

    val opcionesSexo = listOf("Femenino", "Masculino")
    val opcionesActividad = listOf("Sedentario", "Ligero", "Moderado", "Intenso", "Muy intenso")
    val factoresActividad = mapOf(
        "Sedentario" to 1.2f,
        "Ligero" to 1.375f,
        "Moderado" to 1.55f,
        "Intenso" to 1.725f,
        "Muy intenso" to 1.9f
    )

    val fondoGradiente = Brush.linearGradient(
        colors = listOf(Color(0xFFFFCC80), Color(0xFFCE93D8))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoGradiente)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    "Datos personales",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }

            item {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            }

            item {
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = expandedSexo,
                    onExpandedChange = { expandedSexo = !expandedSexo }
                ) {
                    OutlinedTextField(
                        value = sexo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sexo") },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, "Expandir", Modifier.clickable { expandedSexo = !expandedSexo })
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedSexo, onDismissRequest = { expandedSexo = false }) {
                        opcionesSexo.forEach { opcion ->
                            DropdownMenuItem(text = { Text(opcion) }, onClick = {
                                sexo = opcion
                                expandedSexo = false
                            })
                        }
                    }
                }
            }

            item {
                OutlinedTextField(value = estatura, onValueChange = { estatura = it }, label = { Text("Estatura (cm)") }, modifier = Modifier.fillMaxWidth())
            }

            item {
                OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso actual (kg)") }, modifier = Modifier.fillMaxWidth())
            }

            item {
                OutlinedTextField(value = pesoMeta, onValueChange = { pesoMeta = it }, label = { Text("Peso meta (kg)") }, modifier = Modifier.fillMaxWidth())
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = expandedActividad,
                    onExpandedChange = { expandedActividad = !expandedActividad }
                ) {
                    OutlinedTextField(
                        value = nivelActividad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel de actividad") },
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, "Expandir", Modifier.clickable { expandedActividad = !expandedActividad })
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expandedActividad, onDismissRequest = { expandedActividad = false }) {
                        opcionesActividad.forEach { opcion ->
                            DropdownMenuItem(text = { Text(opcion) }, onClick = {
                                nivelActividad = opcion
                                expandedActividad = false
                            })
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        val pesoVal = peso.toFloatOrNull() ?: return@Button
                        val estaturaVal = estatura.toFloatOrNull() ?: return@Button
                        val edadVal = edad.toIntOrNull() ?: return@Button
                        val pesoMetaVal = pesoMeta.toFloatOrNull() ?: return@Button
                        val factorActividad = factoresActividad[nivelActividad] ?: 1.55f

                        if (pesoVal <= pesoMetaVal) {
                            resultadoCalorias = "Actualmente ya estás en o por debajo de tu peso meta."
                            caloriasPerdidasDiarias = "No es necesario perder más peso."
                        } else {
                            val tmb = if (sexo == "Masculino") {
                                10f * pesoVal + 6.25f * estaturaVal - 5f * edadVal + 5f
                            } else {
                                10f * pesoVal + 6.25f * estaturaVal - 5f * edadVal - 161f
                            }

                            val gcdt = tmb * factorActividad
                            val deficitTotal = (pesoVal - pesoMetaVal) * 7700f
                            val deficitDiario = 500f
                            val tiempoDias = (deficitTotal / deficitDiario).toInt()
                            val caloriasObjetivo = (gcdt - deficitDiario).toInt()
                            val caloriasPerdidas = deficitDiario.toInt()

                            resultadoCalorias =
                                "Debes consumir aproximadamente $caloriasObjetivo cal/día para alcanzar tu meta en $tiempoDias días."
                            caloriasPerdidasDiarias = "La pérdida diaria de calorías es de aproximadamente $caloriasPerdidas cal/dia."
                        }

                        mostrarDialogo = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calcula tu meta calórica", color = Color.White)
                }
            }

            item {
                OutlinedButton(
                    onClick = { /* Acción para sincronizar con Google Fit / Apple Health */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sincroniza con Google Fit / Apple Health", color = Color.White)
                }
            }

            item {
                Button(
                    onClick = {
                        userViewModel.nombre.value = nombre
                        userViewModel.edad.value = edad
                        userViewModel.sexo.value = sexo
                        userViewModel.estatura.value = estatura
                        userViewModel.peso.value = peso
                        userViewModel.pesoMeta.value = pesoMeta
                        navController.navigate("menu")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar datos", color = Color.White)
                }
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                confirmButton = {
                    TextButton(onClick = { mostrarDialogo = false }) { Text("OK") }
                },
                title = { Text("Meta calórica", color = Color.Black) },
                text = {
                    Text("$resultadoCalorias\n$caloriasPerdidasDiarias", color = Color.Black)
                }
            )
        }
    }
}
