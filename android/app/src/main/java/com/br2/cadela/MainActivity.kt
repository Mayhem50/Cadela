package com.br2.cadela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
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
import com.br2.cadela.workout.views.WorkoutMainView
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bootstrap()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CadelaTheme {
                // A surface container using the 'background' color from the theme
                ProvideWindowInsets {
                    Surface(color = MaterialTheme.colors.background) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = getString(R.string.nav_splashscreen)
                        ) {
                            composable(route = getString(R.string.nav_splashscreen)) { SplashScreen(navController) }
                            composable(route = getString(R.string.nav_authentication)) { AuthView(navController) }
                            composable(route = getString(R.string.nav_workout_home)) { WorkoutMainView(navController) }
                        }
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
        ).fallbackToDestructiveMigration().build()

//        lifecycleScope.launch(Dispatchers.IO){
//            db.clearAllTables()
//        }

        val api = ApiClient()

        AuthenticationModule.boostrap(db, api)
        WorkoutModule.bootstrap(db)
    }
}