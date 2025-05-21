package com.proyecto.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore by preferencesDataStore(name = "rutina_prefs")

class RutinaDataStore(private val context: Context) {

    private fun keyParaEjercicio(ejercicio: String): Preferences.Key<String> {
        return stringPreferencesKey("fecha_${ejercicio.hashCode()}")
    }

    private val keyRutinaCompletadaHoy = stringPreferencesKey("rutina_completada_hoy")

    suspend fun guardarFecha(ejercicio: String) {
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        context.dataStore.edit { prefs ->
            prefs[keyParaEjercicio(ejercicio)] = hoy
        }
    }

    fun estaCompletadoHoy(ejercicio: String): Flow<Boolean> {
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return context.dataStore.data.map { prefs ->
            prefs[keyParaEjercicio(ejercicio)] == hoy
        }
    }

    suspend fun guardarRutinaCompletadaHoy(fecha: String) {
        context.dataStore.edit { prefs ->
            prefs[keyRutinaCompletadaHoy] = fecha
        }
    }

    fun rutinaCompletadaHoy(): Flow<Boolean> {
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return context.dataStore.data.map { prefs ->
            prefs[keyRutinaCompletadaHoy] == hoy
        }
    }
}
