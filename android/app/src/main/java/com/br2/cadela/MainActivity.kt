package com.br2.cadela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room.databaseBuilder
import com.br2.cadela.authentication.AuthView
import com.br2.cadela.authentication.AuthenticationModule
import com.br2.cadela.shared.ApiClient
import com.br2.cadela.shared.CadelaDatabase
import com.br2.cadela.ui.theme.CadelaTheme
import com.br2.cadela.workout.WorkoutModule
import com.br2.cadela.workout.WorkoutView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bootstrap()

        setContent {
            CadelaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash-screen"
                    ) {
                        composable("splash-screen") { WorkoutView() }
                        composable("authentication") { AuthView() }
                    }
                }
            }
        }
    }

    private fun bootstrap() {
        val db = databaseBuilder(
            applicationContext,
            CadelaDatabase::class.java,
            getString(R.string.database_name)
        ).build()
        val api = ApiClient()

        AuthenticationModule.boostrap(db, api)
        WorkoutModule.bootstrap(db)
    }
}