package com.proyecto.myapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var nombre = mutableStateOf("")
    var edad = mutableStateOf("")
    var sexo = mutableStateOf("")
    var estatura = mutableStateOf("")
    var peso = mutableStateOf("")
    var pesoMeta = mutableStateOf("")
    var contrasena = mutableStateOf("")

    // Resultados de los cálculos
    var caloriasQuemadas = mutableStateOf(0)
    var tiempoEstimadoDias = mutableStateOf(0)
    var caloriasDiarias = mutableStateOf(0)

    // Función para registrar o actualizar los datos del usuario
    fun registrarUsuario(
        nombreUsuario: String,
        edadUsuario: String,
        sexoUsuario: String,
        estaturaUsuario: String,
        pesoUsuario: String,
        pesoMetaUsuario: String,
        contrasenaUsuario: String
    ) {
        // Actualizamos los valores en el ViewModel
        nombre.value = nombreUsuario
        edad.value = edadUsuario
        sexo.value = sexoUsuario
        estatura.value = estaturaUsuario
        peso.value = pesoUsuario
        pesoMeta.value = pesoMetaUsuario
        contrasena.value = contrasenaUsuario
    }

    // Función para calcular las calorías y el tiempo estimado de pérdida
    fun calcularMetaCalorica() {
        val pesoVal = peso.value.toFloatOrNull() ?: return
        val estaturaVal = estatura.value.toFloatOrNull() ?: return
        val edadVal = edad.value.toIntOrNull() ?: return
        val pesoMetaVal = pesoMeta.value.toFloatOrNull() ?: return

        val factorActividad = 1.55f // Asumiendo un nivel de actividad moderado (ajústalo según la selección del usuario)
        val tmb = if (sexo.value == "Masculino") {
            10f * pesoVal + 6.25f * estaturaVal - 5f * edadVal + 5f
        } else {
            10f * pesoVal + 6.25f * estaturaVal - 5f * edadVal - 161f
        }

        val gcdt = tmb * factorActividad
        val deficitTotal = (pesoVal - pesoMetaVal) * 7700f
        val deficitDiario = 500f // Déficit de 500 calorías por día para perder peso de manera saludable
        val tiempoDias = (deficitTotal / deficitDiario).toInt()
        val caloriasObjetivo = (gcdt - deficitDiario).toInt()

        caloriasQuemadas.value = gcdt.toInt()
        caloriasDiarias.value = caloriasObjetivo
        tiempoEstimadoDias.value = tiempoDias
    }
    var diasCompletados = mutableStateOf(0)

    fun incrementarDiasCompletados() {
        diasCompletados.value += 1
    }

}



