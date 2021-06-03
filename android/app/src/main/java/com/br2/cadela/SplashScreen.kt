package com.br2.cadela

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.br2.cadela.ui.theme.Red200
import com.br2.cadela.ui.theme.Red500
import kotlinx.coroutines.*

@Composable
fun SplashScreen(navController: NavController) {
    val infinitelyAnimatedFloat = animatedLoop(800, 0.65f, 1f)
    Box(
        modifier = Modifier.background(
            brush = Brush.linearGradient(
                listOf(
                    Red500,
                    Red200
                ), start = Offset.Zero, end = Offset(1f, 1f)
            )
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_without_gradient),
                contentDescription = null,
                Modifier
                    .width(Dp(120f))
                    .height(Dp(120f)),
                alignment = Alignment.Center,
                alpha = infinitelyAnimatedFloat.value
            )
        }
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