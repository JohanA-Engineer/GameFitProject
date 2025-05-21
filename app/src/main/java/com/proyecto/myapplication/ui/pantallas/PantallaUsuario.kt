package com.proyecto.myapplication.ui.pantallas

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.dimensionResource
//import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.proyecto.myapplication.viewmodel.UserViewModel

@SuppressLint("DefaultLocale")
@Composable
fun PantallaUsuario(navController: NavHostController, userViewModel: UserViewModel) {
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imagenUri = uri }

    val peso = userViewModel.peso.value.toFloatOrNull() ?: 0f
    val estaturaCm = userViewModel.estatura.value.toFloatOrNull() ?: 0f
    val estaturaM = estaturaCm / 100
    val imc = if (estaturaM > 0) peso / (estaturaM * estaturaM) else 0f

    val pesoMeta = userViewModel.pesoMeta.value.toFloatOrNull() ?: peso
    val pesoInicial = peso
    val progreso = if ((pesoInicial - pesoMeta) != 0f)
        ((pesoInicial - peso) / (pesoInicial - pesoMeta)) * 100 else 0f

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFD180), Color(0xFFCE93D8)) // Naranja claro a morado suave
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(32.dp)
            .systemBarsPadding() // Asegura que no se solape con el notch o las barras de sistema
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Perfil del Usuario", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // Foto de perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imagenUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imagenUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape) // Imagen circular
                    )
                } else {
                    Text("Agregar Foto", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            if (imagenUri != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { imagenUri = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar Foto", color = MaterialTheme.colorScheme.onError)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Nombre: ${userViewModel.nombre.value}", fontSize = 20.sp)
            Text("Edad: ${userViewModel.edad.value} años", fontSize = 20.sp)
            Text("Sexo: ${userViewModel.sexo.value}", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text("IMC: ${String.format("%.1f", imc)}", fontSize = 18.sp)
            Text("Progreso hacia tu meta: ${progreso.toInt()}%", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate("menu")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al menú")
            }
        }
    }
}
