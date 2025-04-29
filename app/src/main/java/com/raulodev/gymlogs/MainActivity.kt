package com.raulodev.gymlogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.raulodev.gymlogs.database.AppDatabase
import com.raulodev.gymlogs.ui.theme.GymLogsTheme

class MainActivity : ComponentActivity() {

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.db = AppDatabase.getDatabase(this.applicationContext)
        setContent {
            GymLogsTheme {
                App(modifier = Modifier.fillMaxSize(), db)
            }
        }
    }
}





