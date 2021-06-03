package com.br2.cadela

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.br2.cadela.authentication.AuthView
import com.br2.cadela.authentication.signin.SigninApi
import com.br2.cadela.authentication.signin.SigninService
import com.br2.cadela.authentication.signin.TokenRepository
import com.br2.cadela.shared.Api
import com.br2.cadela.shared.CadelaDatabase
import com.br2.cadela.ui.theme.CadelaTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var signinService: SigninService
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bootstrap()



        setContent {
            CadelaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = rememberNavController(), startDestination = "authentication" ){
                        composable("authentication") {
                            AuthView()
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
        ).build()
        val api = Api(SigninApi::class.java)

        val tokenRepository = TokenRepository(db.tokenDao())
        signinService = SigninService(api, tokenRepository)
    }
}