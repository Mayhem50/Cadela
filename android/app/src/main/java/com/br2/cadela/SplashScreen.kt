package com.br2.cadela

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.coroutines.*

@Composable
fun SplashScreen(navController: NavController) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, Modifier.width(Dp(120f)).height(Dp(120f)), alignment = Alignment.Center)
    }
    waitBeforeNavigate(5000, navController)
}

fun waitBeforeNavigate(timeMs: Long, navController: NavController) =
    CoroutineScope(Dispatchers.IO).launch {
        delay(timeMs)
        withContext(Dispatchers.Main) {
            navController.navigate(
                "authentication",
                NavOptions.Builder().setPopUpTo("splash-screen", true).build()
            )
        }
    }