package com.proyecto.myapplication.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MonedasViewModel : ViewModel() {
    // Monedas compartidas entre pantallas (inicial 500)
    private val _monedas = MutableStateFlow(500)
    val monedas: StateFlow<Int> = _monedas

    // Función para agregar monedas
    fun agregarMonedas(cantidad: Int) {
        _monedas.value += cantidad
    }

    // Función para restar monedas (si hay suficientes)
    fun restarMonedas(cantidad: Int) {
        if (_monedas.value >= cantidad) {
            _monedas.value -= cantidad
        }
    }
}
