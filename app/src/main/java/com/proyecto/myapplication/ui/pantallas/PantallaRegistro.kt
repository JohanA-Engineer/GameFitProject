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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(navController: NavHostController, userViewModel: UserViewModel) {
    var nombreUsuario by remember { mutableStateOf("") }
    var edadUsuario by remember { mutableStateOf("") }
    var sexoUsuario by remember { mutableStateOf("") }
    var estaturaUsuario by remember { mutableStateOf("") }
    var pesoUsuario by remember { mutableStateOf("") }
    var pesoMetaUsuario by remember { mutableStateOf("") }
    var contrasenaUsuario by remember { mutableStateOf("") }
    var confirmarContrasenaUsuario by remember { mutableStateOf("") }
    var errorContrasena by remember { mutableStateOf(false) }
    val opcionesSexo = listOf("Masculino", "Femenino")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFD180),
                        Color(0xFFCE93D8)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombreUsuario,
                onValueChange = { nombreUsuario = it },
                label = { Text("Nombre de usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = edadUsuario,
                onValueChange = { edadUsuario = it },
                label = { Text("Edad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = sexoUsuario,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sexo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    opcionesSexo.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                sexoUsuario = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = estaturaUsuario,
                onValueChange = { estaturaUsuario = it },
                label = { Text("Estatura (cm)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = pesoUsuario,
                onValueChange = { pesoUsuario = it },
                label = { Text("Peso (kg)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = pesoMetaUsuario,
                onValueChange = { pesoMetaUsuario = it },
                label = { Text("Peso meta (kg)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contrasenaUsuario,
                onValueChange = { contrasenaUsuario = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmarContrasenaUsuario,
                onValueChange = { confirmarContrasenaUsuario = it },
                label = { Text("Confirmar Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorContrasena) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (contrasenaUsuario == confirmarContrasenaUsuario) {
                        userViewModel.registrarUsuario(
                            nombreUsuario,
                            edadUsuario,
                            sexoUsuario,
                            estaturaUsuario,
                            pesoUsuario,
                            pesoMetaUsuario,
                            contrasenaUsuario
                        )
                        navController.navigate("menu")
                    } else {
                        errorContrasena = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("¿Ya tienes una cuenta?")
            TextButton(onClick = { navController.navigateUp() }) {
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
