package com.proyecto.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.proyecto.myapplication.ui.theme.MyApplicationTheme
import com.proyecto.myapplication.navegacion.AppNavigation
import com.proyecto.myapplication.viewmodel.UserViewModel
import com.proyecto.myapplication.viewmodel.MonedasViewModel   // ✅ IMPORTANTE

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val monedasViewModel: MonedasViewModel by viewModels()  // ✅ NUEVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    userViewModel = userViewModel,
                    monedasViewModel = monedasViewModel  // ✅ PASARLO AQUÍ
                )
            }
        }
    }
}