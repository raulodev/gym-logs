package com.raulodev.gymlogs.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.raulodev.gymlogs.database.AppDatabase
import com.raulodev.gymlogs.database.Gym
import com.raulodev.gymlogs.enums.Routes
import com.raulodev.gymlogs.ui.components.CustomButton
import com.raulodev.gymlogs.ui.components.Input
import com.raulodev.gymlogs.ui.theme.GymLogsTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    db: AppDatabase? = null, navegationController: NavHostController? = null
) {

    val scope = rememberCoroutineScope()
    var gymName by rememberSaveable { mutableStateOf("") }

    suspend fun addGym() {
        if (gymName.isEmpty()) {
            return
        }

        val record = Gym(name = gymName)

        try {

            db?.gymDao()?.insert(record)
            navegationController?.navigate(Routes.UsersScreen.name)

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = "Gym Logs",
                fontSize = 45.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Input(
                value = gymName,
                onValueChange = { gymName = it },
                placeholder = "Nombre del gimnasio"
            )
            CustomButton(label = "Continuar", onClick = { scope.launch { addGym() } })
        }
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_YES, name = "HomeDark", showBackground = true
)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    GymLogsTheme {
        Surface {
            HomeScreen()
        }
    }
}


