package com.proyecto.myapplication.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.proyecto.myapplication.viewmodel.MonedasViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val FILAS = 20
const val COLUMNAS = 10

@Composable
fun PantallaJuego(navController: NavHostController, monedasViewModel: MonedasViewModel) {
    val grid = remember { mutableStateListOf<MutableList<Boolean>>() }
    val piezaActual = remember { mutableStateOf(Pieza.random()) }
    var posX by remember { mutableIntStateOf(COLUMNAS / 2) }
    var posY by remember { mutableIntStateOf(0) }
    val juegoTerminado = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var puntaje by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        resetGrid(grid)
    }

    fun moverPieza(dX: Int, dY: Int) {
        if (juegoTerminado.value) return

        if (puedeMover(piezaActual.value, grid, posX + dX, posY + dY)) {
            posX += dX
            posY += dY
        } else if (dY == 1) {
            fijarPieza(piezaActual.value, grid, posX, posY)
            val eliminadas = eliminarFilasCompletas(grid)
            puntaje += eliminadas * 100
            monedasViewModel.agregarMonedas(eliminadas * 50) // 💰 50 monedas por fila eliminada
            piezaActual.value = Pieza.random()
            posX = COLUMNAS / 2
            posY = 0

            if (!puedeMover(piezaActual.value, grid, posX, posY)) {
                juegoTerminado.value = true
            }
        }
    }

    fun reiniciarJuego() {
        resetGrid(grid)
        piezaActual.value = Pieza.random()
        posX = COLUMNAS / 2
        posY = 0
        juegoTerminado.value = false
        puntaje = 0
    }

    fun rotarPieza() {
        val rotada = piezaActual.value.rotada()
        if (puedeMover(rotada, grid, posX, posY)) {
            piezaActual.value = rotada
        }
    }

    // Movimiento automático hacia abajo
    LaunchedEffect(piezaActual.value) {
        scope.launch {
            while (!juegoTerminado.value) {
                delay(1000)
                moverPieza(0, 1)
            }
        }
    }

    // Añadimos la corrección del notch y espacio para la barra de estado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()) // Respetar el notch y las barras del sistema
            .padding(16.dp), // Ajuste para todo el contenido
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("Mini Tetris 🎮", style = MaterialTheme.typography.headlineMedium)
        Text("Puntos: $puntaje", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (juegoTerminado.value) {
            Text("¡Juego Terminado!", color = Color.Red, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { reiniciarJuego() }) {
                Text("Reiniciar juego")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Grid de juego
        Column {
            for (y in 0 until FILAS) {
                Row {
                    for (x in 0 until COLUMNAS) {
                        val ocupado = grid.getOrNull(y)?.getOrNull(x) == true ||
                                piezaActual.value.shape.any { (dx, dy) ->
                                    x == posX + dx && y == posY + dy
                                }
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(if (ocupado) Color.Blue else Color.LightGray)
                                .padding(1.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Controles
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { moverPieza(-1, 0) }, enabled = !juegoTerminado.value) {
                Text("⬅️")
            }
            Button(onClick = { moverPieza(1, 0) }, enabled = !juegoTerminado.value) {
                Text("➡️")
            }
            Button(onClick = { moverPieza(0, 1) }, enabled = !juegoTerminado.value) {
                Text("⬇️")
            }
            Button(onClick = { rotarPieza() }, enabled = !juegoTerminado.value) {
                Text("🔄")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}

// PIEZAS

data class Pieza(val shape: List<Pair<Int, Int>>) {
    fun rotada(): Pieza {
        val nuevaForma = shape.map { (x, y) -> -y to x } // Rotación 90° antihorario
        return Pieza(nuevaForma)
    }

    companion object {
        fun random(): Pieza {
            val figuras = listOf(
                listOf(0 to 0, 1 to 0, 0 to 1, 1 to 1), // Cuadrado (O)
                listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0), // Barra (I)
                listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1), // L invertida
                listOf(1 to 0, 0 to 1, 1 to 1, 2 to 1)  // T
            )
            return Pieza(figuras.random())
        }
    }
}

// FUNCIONES AUXILIARES

fun resetGrid(grid: MutableList<MutableList<Boolean>>) {
    grid.clear()
    repeat(FILAS) {
        grid.add(MutableList(COLUMNAS) { false })
    }
}

fun puedeMover(pieza: Pieza, grid: List<List<Boolean>>, newX: Int, newY: Int): Boolean {
    return pieza.shape.all { (dx, dy) ->
        val x = newX + dx
        val y = newY + dy
        x in 0 until COLUMNAS && y in 0 until FILAS && !grid[y][x]
    }
}

fun fijarPieza(pieza: Pieza, grid: MutableList<MutableList<Boolean>>, posX: Int, posY: Int) {
    pieza.shape.forEach { (dx, dy) ->
        val x = posX + dx
        val y = posY + dy
        if (y in 0 until FILAS && x in 0 until COLUMNAS) {
            grid[y][x] = true
        }
    }
}

fun eliminarFilasCompletas(grid: MutableList<MutableList<Boolean>>): Int {
    val filasCompletas = grid.filter { fila -> fila.all { it } }
    if (filasCompletas.isNotEmpty()) {
        filasCompletas.forEach { grid.remove(it) }
        repeat(filasCompletas.size) {
            grid.add(0, MutableList(COLUMNAS) { false })
        }
    }
    return filasCompletas.size
}
