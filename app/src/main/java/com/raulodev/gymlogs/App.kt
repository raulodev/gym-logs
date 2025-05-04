package com.raulodev.gymlogs

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raulodev.gymlogs.database.AppDatabase
import com.raulodev.gymlogs.enums.Routes
import com.raulodev.gymlogs.screens.ChartScreen
import com.raulodev.gymlogs.screens.HomeScreen
import com.raulodev.gymlogs.screens.UsersScreen
import com.raulodev.gymlogs.screens.SplashScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@Composable
fun App(
    modifier: Modifier, db: AppDatabase
) {

    var keepSplashScreen by rememberSaveable { mutableStateOf(true) }
    var gymCount by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        try {
            val count = withContext(Dispatchers.IO) {
                db.gymDao().count()
            }
            gymCount = count ?: 0

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
        delay(500)
        keepSplashScreen = false
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Surface(
            modifier = modifier.padding(innerPadding)
        ) {
            val navegationController = rememberNavController()
            NavHost(
                navController = navegationController,
                startDestination = if (keepSplashScreen) Routes.SplashScreen.name else if (gymCount == 0) Routes.HomeScreen.name else Routes.UsersScreen.name
            ) {
                composable(Routes.SplashScreen.name) {
                    SplashScreen()
                }
                composable(Routes.HomeScreen.name) {
                    HomeScreen(
                        db, navegationController
                    )
                }
                composable(Routes.UsersScreen.name) {
                    UsersScreen(
                        db, navegationController
                    )
                }
                composable(Routes.ChartsScreen.name) {
                    ChartScreen(db, navegationController)

                }
            }
        }
    }
}

